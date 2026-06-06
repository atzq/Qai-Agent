package net.itzq.mira.modules.vfs;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

/**
 * 基于 Jimfs 的内存虚拟文件系统工具类。
 * <p>
 * 使用示例：
 * <pre>{@code
 * try (Var_VFS vfs = Var_VFS.createUnix()) {
 *     vfs.write("/data/hello.txt", "Hello Var_VFS");
 *     vfs.write("/data/sub/info.txt", "Nested file");
 *     System.out.println(vfs.listTree());
 *     String content = vfs.readString("/data/hello.txt");
 * }
 * }</pre>
 *
 * @author tangzq
 */
public class VFS implements Closeable {

    private final FileSystem fs;

    private VFS(FileSystem fs) {
        this.fs = fs;
    }

    // ==================== 1. 创建内存 Var_VFS ====================

    /** 创建一个默认（类 Unix）配置的内存文件系统 */
    public static VFS create() {
        return new VFS(Jimfs.newFileSystem(Configuration.unix()));
    }

    /** 创建一个 Unix 风格的内存文件系统 */
    public static VFS createUnix() {
        return new VFS(Jimfs.newFileSystem(Configuration.unix()));
    }

    /** 创建一个 Windows 风格的内存文件系统 */
    public static VFS createWindows() {
        return new VFS(Jimfs.newFileSystem(Configuration.windows()));
    }

    /** 使用自定义 Jimfs 配置创建内存文件系统 */
    public static VFS create(Configuration config) {
        return new VFS(Jimfs.newFileSystem(config));
    }

    // ==================== 2. 文件读写 ====================

    /**
     * 将字节数组写入指定路径的文件，若父目录不存在则自动创建。
     *
     * @param path 文件路径，如 "/dir/file.txt"
     * @param data 要写入的字节内容
     */
    public void write(String path, byte[] data) throws IOException {
        Path p = fs.getPath(path);
        if (p.getParent() != null) {
            Files.createDirectories(p.getParent());
        }
        Files.write(p, data);
    }

    /**
     * 将字符串以 UTF-8 编码写入文件。
     *
     * @param path    文件路径
     * @param content 字符串内容
     */
    public void write(String path, String content) throws IOException {
        write(path, content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 将字符串以指定编码写入文件。
     *
     * @param path    文件路径
     * @param content 字符串内容
     * @param charset 字符编码
     */
    public void write(String path, String content, Charset charset) throws IOException {
        write(path, content.getBytes(charset));
    }

    /**
     * 读取文件全部字节。
     *
     * @param path 文件路径
     * @return 文件内容的字节数组
     */
    public byte[] readAllBytes(String path) throws IOException {
        return Files.readAllBytes(fs.getPath(path));
    }

    /**
     * 以 UTF-8 编码读取文件内容为字符串。
     *
     * @param path 文件路径
     * @return 文件内容字符串
     */
    public String readString(String path) throws IOException {
        return readString(path, StandardCharsets.UTF_8);
    }

    /**
     * 以指定编码读取文件内容为字符串。
     *
     * @param path    文件路径
     * @param charset 字符编码
     * @return 文件内容字符串
     */
    public String readString(String path, Charset charset) throws IOException {
        return new String(readAllBytes(path), charset);
    }

    // ==================== 3. 目录与文件信息 ====================

    /**
     * 递归列出虚拟文件系统中所有文件和目录的树状结构。
     *
     * @return 以换行分隔的目录树字符串（目录以 '/' 结尾）
     */
    public String listTree() throws IOException {
        StringBuilder sb = new StringBuilder();
        Path root = fs.getPath("/");
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                sb.append(dir.toString()).append("/\n");
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                sb.append(file.toString()).append("\n");
                return FileVisitResult.CONTINUE;
            }
        });
        return sb.toString();
    }

    /**
     * 列出指定目录下的直接子项（文件/目录）名称，不递归。
     * <p>
     * 返回的是每个子项的文件名（如 "word", "[Content_Types].xml"），不是完整路径。
     * 调用方拼接路径时应使用 dir + "/" + name。
     * <p>
     * 返回的 Stream 底层持有目录句柄，使用完毕后必须关闭。
     * 推荐使用 try-with-resources：
     * <pre>{@code
     * try (Stream<String> children = vfs.list("/docx")) {
     *     children.forEach(name -> System.out.println(name));
     * }
     * }</pre>
     *
     * @param dir 目录路径
     * @return 包含子项文件名的流（需手动关闭）
     */
    public Stream<String> list(String dir) throws IOException {
        Path d = fs.getPath(dir);
        if (!Files.isDirectory(d)) {
            throw new NotDirectoryException(dir);
        }
        return Files.list(d).map(p -> p.getFileName().toString());
    }

    /**
     * 判断文件或目录是否存在。
     */
    public boolean exists(String path) {
        return Files.exists(fs.getPath(path));
    }

    /**
     * 判断路径是否为目录。
     */
    public boolean isDirectory(String path) {
        return Files.isDirectory(fs.getPath(path));
    }

    /**
     * 判断路径是否为普通文件。
     */
    public boolean isRegularFile(String path) {
        return Files.isRegularFile(fs.getPath(path));
    }

    /**
     * 获取文件大小（字节数）。
     */
    public long size(String path) throws IOException {
        return Files.size(fs.getPath(path));
    }

    // ==================== 4. 文件/目录操作 ====================

    /**
     * 创建目录，若父目录不存在则一并创建。
     */
    public void createDirectories(String dir) throws IOException {
        Files.createDirectories(fs.getPath(dir));
    }

    /**
     * 删除文件或空目录。
     */
    public void delete(String path) throws IOException {
        Files.delete(fs.getPath(path));
    }

    /**
     * 如果存在则删除文件或空目录。
     *
     * @return 是否确实删除了文件
     */
    public boolean deleteIfExists(String path) throws IOException {
        return Files.deleteIfExists(fs.getPath(path));
    }

    /**
     * 递归删除目录及其下所有内容。
     */
    public void deleteRecursively(String path) throws IOException {
        Path p = fs.getPath(path);
        if (Files.isDirectory(p)) {
            try (Stream<Path> walk = Files.walk(p)) {
                walk.sorted(java.util.Comparator.reverseOrder())
                        .forEach(child -> {
                            try {
                                Files.delete(child);
                            } catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
            }
        } else {
            Files.delete(p);
        }
    }

    /**
     * 复制文件或目录（非递归）。
     */
    public void copy(String source, String target, CopyOption... options) throws IOException {
        Files.copy(fs.getPath(source), fs.getPath(target), options);
    }

    /**
     * 移动/重命名文件或目录。
     */
    public void move(String source, String target, CopyOption... options) throws IOException {
        Files.move(fs.getPath(source), fs.getPath(target), options);
    }

    // ==================== 5. 释放 Var_VFS ====================

    /**
     * 关闭并释放此虚拟文件系统占用的内存资源。
     * 推荐使用 try-with-resources 自动关闭。
     */
    @Override
    public void close() throws IOException {
        fs.close();
    }

    /**
     * 获取内部的 Jimfs FileSystem 对象，以便直接使用 NIO 操作。
     */
    public FileSystem getFileSystem() {
        return fs;
    }
}
