package net.itzq.mira.modules.ai.client.sse;

import lombok.extern.slf4j.Slf4j;
import net.itzq.mira.modules.ai.client.handle.HttpStreamEventInterface;
import org.asynchttpclient.*;
import org.asynchttpclient.handler.TransferCompletionHandler;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 *  HttpSSEClient
 *
 *  @author tangzq
 */
@Slf4j
public class HttpSSEClient {

    private static volatile HttpSSEClient instance;

    private volatile AsyncHttpClient asyncHttpClient;

    private static final Object LOCK = new Object();

    /**
     * 私有构造方法
     */
    private HttpSSEClient() {
        this.asyncHttpClient = createDefaultClient();
    }
    private static AsyncHttpClient createDefaultClient() {
        return Dsl.asyncHttpClient(Dsl.config()
                // 客户端尝试与服务器建立 TCP 连接的最大等待时间。
                .setConnectTimeout(15 * 1000)  // 15 秒
                // 在连接建立成功后，等待服务器返回数据的最大空闲时间。如果两次数据包之间的间隔超过 15 分钟，连接会被关闭。
                // 对 SSE 这类长连接而言，这个值过短会导致连接意外断开。尤其是同步工具调用
                .setReadTimeout(15 * 60 * 1000) // 15 分钟
                // 从发起请求到接收完整响应的总时长上限，包含连接、发送请求、等待响应的全部时间。
                // 超过 15 分钟未完成整个请求，会超时失败。
                .setRequestTimeout(15 * 60 * 1000)       // 15 分钟
                // 该客户端实例可以同时打开的最大连接数（所有目标主机合计）。超过后，新请求会排队等待
                .setMaxConnections(100)
                // 连接在池中保持空闲状态（没有请求使用）的最长时间。超过 60 秒未被复用，连接会被关闭并从池中移除。
                .setPooledConnectionIdleTimeout(60 * 1000)   // 60 秒
                .setKeepAlive(false) // SSE场景不建议keepalive
                .build());
    }

    /**
     * 获取单例实例
     */
    public static HttpSSEClient getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new HttpSSEClient();
                }
            }
        }
        return instance;
    }

    /**
     * 重新设置 AsyncHttpClient（外部自定义配置）
     * 会关闭旧的 client，请确保没有进行中的请求
     */
    public void setAsyncHttpClient(AsyncHttpClient newClient) {
        if (newClient == null) {
            throw new IllegalArgumentException("AsyncHttpClient 不能为 null");
        }
        AsyncHttpClient old = this.asyncHttpClient;
        this.asyncHttpClient = newClient;
        if (old != null && old != newClient) {
            try {
                old.close();
            } catch (Exception e) {
                log.warn("关闭旧 AsyncHttpClient 失败: {}", e.getMessage());
            }
        }
    }

    public AsyncHttpClient getAsyncHttpClient() {
        return asyncHttpClient;
    }

    /**
     * 重置为默认配置的 client
     */
    public void resetToDefault() {
        setAsyncHttpClient(createDefaultClient());
    }

    /**
     * ==================== 同步请求方法 ====================
     */

    /**
     * 同步GET请求
     * @param url 请求URL
     * @return 响应字符串
     */
    public String getSync(String url) {
        return getSync(url, null);
    }

    /**
     * 同步GET请求（带请求头）
     * @param url 请求URL
     * @param headers 请求头
     * @return 响应字符串
     */
    public String getSync(String url, Map<String, String> headers) {
        try {
            BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);
            addHeaders(requestBuilder, headers);

            Response response = requestBuilder.execute().get();
            return handleResponse(response);
        } catch (Exception e) {
            log.error("同步GET请求失败: {}", e.getMessage(), e);
            throw new RuntimeException("HTTP请求失败", e);
        }
    }

    /**
     * 同步GET请求（带超时）
     * @param url 请求URL
     * @param timeout 超时时间（毫秒）
     * @return 响应字符串
     */
    public String getSync(String url, long timeout) {
        return getSync(url, null, timeout);
    }

    /**
     * 同步GET请求（带请求头和超时）
     */
    public String getSync(String url, Map<String, String> headers, long timeout) {
        try {
            BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url);
            addHeaders(requestBuilder, headers);

            Response response = requestBuilder.execute().get(timeout, TimeUnit.MILLISECONDS);
            return handleResponse(response);
        } catch (Exception e) {
            log.error("同步GET请求失败: {}", e.getMessage(), e);
            throw new RuntimeException("HTTP请求失败", e);
        }
    }

    /**
     * 同步POST请求（JSON格式）
     * @param url 请求URL
     * @param jsonBody JSON请求体
     * @return 响应字符串
     */
    public String postJsonSync(String url, String jsonBody) {
        return postJsonSync(url, jsonBody, null);
    }

    /**
     * 同步POST请求（JSON格式，带请求头）
     */
    public String postJsonSync(String url, String jsonBody, Map<String, String> headers) {
        try {
            BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url)
                    .setHeader("Content-Type", "application/json")
                    .setBody(jsonBody.getBytes(StandardCharsets.UTF_8));

            addHeaders(requestBuilder, headers);

            Response response = requestBuilder.execute().get();
            return handleResponse(response);
        } catch (Exception e) {
            log.error("同步POST请求失败: {}", e.getMessage(), e);
            throw new RuntimeException("HTTP请求失败", e);
        }
    }

    /**
     * 同步POST请求（表单格式）
     * @param url 请求URL
     * @param formParams 表单参数
     * @return 响应字符串
     */
    public String postFormSync(String url, Map<String, String> formParams) {
        return postFormSync(url, formParams, null);
    }

    /**
     * 同步POST请求（表单格式，带请求头）
     */
    public String postFormSync(String url, Map<String, String> formParams, Map<String, String> headers) {
        try {
            BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url)
                    .setHeader("Content-Type", "application/x-www-form-urlencoded");

            // 添加表单参数
            StringBuilder formBody = new StringBuilder();
            if (formParams != null) {
                formParams.forEach((key, value) -> {
                    if (formBody.length() > 0) {
                        formBody.append("&");
                    }
                    formBody.append(key).append("=").append(value);
                });
            }
            requestBuilder.setBody(formBody.toString());

            addHeaders(requestBuilder, headers);

            Response response = requestBuilder.execute().get();
            return handleResponse(response);
        } catch (Exception e) {
            log.error("同步POST表单请求失败: {}", e.getMessage(), e);
            throw new RuntimeException("HTTP请求失败", e);
        }
    }

    /**
     * ==================== 辅助方法 ====================
     */

    /**
     * 添加请求头
     */
    private void addHeaders(BoundRequestBuilder requestBuilder, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(requestBuilder::setHeader);
        }
    }

    /**
     * 处理响应
     */
    private String handleResponse(Response response) {
        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
        } else {
            String responseBody = response.getResponseBody(StandardCharsets.UTF_8);
            log.error("HTTP请求返回错误状态码: {}", response.getStatusCode());
            log.error("错误响应内容: {}", responseBody);
        }
        return response.getResponseBody(StandardCharsets.UTF_8);
    }

    /**
     * 获取原生AsyncHttpClient实例
     */
    public AsyncHttpClient getClient() {
        return asyncHttpClient;
    }

    /**
     * SSE响应解析器
     */
    private static class SseResponseHandler extends TransferCompletionHandler {
        private final HttpStreamEventInterface eventHandler;
        private final StringBuilder buffer = new StringBuilder();
        private String currentEventType = "message";
        private String currentId = "";

        // ----- 新增字段 -----
        private boolean statusError = false;                // 标记非 2xx 状态
        private final StringBuilder errorBody = new StringBuilder();  // 收集错误响应体
        // ------------------

        public SseResponseHandler(HttpStreamEventInterface eventHandler) {
            this.eventHandler = eventHandler;
        }

        @Override
        public State onStatusReceived(HttpResponseStatus status) throws Exception {
            int statusCode = status.getStatusCode();
            String statusText = status.getStatusText();
            String uri = status.getUri().toString();
            log.info("SSE请求响应状态 - URL: {}, 状态码: {}, 状态描述: {}", uri, statusCode, statusText);

            if (statusCode < 200 || statusCode >= 300) {
                statusError = true;   // 标记为错误状态，后续不再走 SSE 解析
                log.warn("SSE请求返回非成功状态码 - URL: {}, 状态码: {}, 开始收集错误响应体", uri, statusCode);
            }

            return super.onStatusReceived(status);   // 不再抛异常，让数据继续到达
        }

        @Override
        public State onBodyPartReceived(HttpResponseBodyPart content) throws Exception {
            String chunk = new String(content.getBodyPartBytes(), StandardCharsets.UTF_8);

            if (statusError) {
                // 错误场景：只累积，不做 SSE 解析
                errorBody.append(chunk);
            } else {
                // 正常场景：原逻辑不变
                buffer.append(chunk);
                processBuffer();
            }

            return super.onBodyPartReceived(content);
        }

        private void processBuffer() {
            // 只在 !statusError 时调用，逻辑保持不变
            String content = buffer.toString();
            int index;
            while ((index = content.indexOf("\n")) != -1) {
                String line = content.substring(0, index);
                content = content.substring(index + 1);

                if (line.isEmpty()) {
                    resetEvent();
                } else if (line.startsWith(":")) {
                    eventHandler.onComment(line.substring(1).trim());
                } else if (line.startsWith("event:")) {
                    currentEventType = line.substring(6).trim();
                } else if (line.startsWith("data:")) {
                    String data = line.substring(5).trim();
                    eventHandler.onEvent(currentEventType, data, currentId);
                } else if (line.startsWith("id:")) {
                    currentId = line.substring(3).trim();
                }
                // retry: 可以忽略
            }
            buffer.setLength(0);
            buffer.append(content);
        }

        private void resetEvent() {
            currentEventType = "message";
            currentId = "";
        }

        @Override
        public Response onCompleted(Response response) throws Exception {
            if (statusError) {
                // 打印完整错误响应体并抛出异常，触发 onThrowable -> eventHandler.onError
                String body = errorBody.toString();
                log.error("SSE请求最终失败，URL: {}, 状态码: {}, 响应内容: {}",
                        response.getUri(), response.getStatusCode(), body);
                throw new RuntimeException(
                        String.format("SSE请求失败，状态码: %s, 响应体: %s", response.getStatusCode(), body));
            }

            eventHandler.onComplete();
            return super.onCompleted(response);
        }

        @Override
        public void onThrowable(Throwable t) {
            eventHandler.onError(t);
        }
    }

    /**
     * 发送SSE请求（同步方式）
     * @param url 请求URL
     * @param eventHandler SSE事件处理器
     */
    public void getSseSync(String url, HttpStreamEventInterface eventHandler) {
        try {
            SseResponseHandler handler = new SseResponseHandler(eventHandler);
            ListenableFuture<Response> future = asyncHttpClient.prepareGet(url)
                    .setHeader("Accept", "text/event-stream")
                    .setHeader("Cache-Control", "no-cache")
                    .execute(handler);

            future.get(); // 等待完成

        } catch (Exception e) {
            log.error("SSE请求失败: {}", e.getMessage(), e);
            eventHandler.onError(e);
        }
    }

    /**
     * 发送SSE请求（异步方式）
     * @param url 请求URL
     * @param eventHandler SSE事件处理器
     * @return CompletableFuture<Void>
     */
    public CompletableFuture<Void> getSseAsync(String url, HttpStreamEventInterface eventHandler) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            SseResponseHandler handler = new SseResponseHandler(new HttpStreamEventInterface() {
                @Override
                public void onEvent(String eventType, String data, String id) {
                    try {
                        eventHandler.onEvent(eventType, data, id);
                    } catch (Exception e) {
                        log.error("处理SSE事件时出错: {}", e.getMessage(), e);
                    }
                }

                @Override
                public void onComment(String comment) {
                    eventHandler.onComment(comment);
                }

                @Override
                public void onComplete() {
                    try {
                        eventHandler.onComplete();
                        future.complete(null);
                    } catch (Exception e) {
                        future.completeExceptionally(e);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    try {
                        eventHandler.onError(t);
                    } catch (Exception e) {
                        log.error("处理错误时出错: {}", e.getMessage(), e);
                    }
                    future.completeExceptionally(t);
                }
            });

            asyncHttpClient.prepareGet(url)
                    .setHeader("Accept", "text/event-stream")
                    .setHeader("Cache-Control", "no-cache")
                    .execute(handler);

        } catch (Exception e) {
            log.error("发起SSE请求失败: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    /**
     * 发送带自定义头的SSE请求
     * @param url 请求URL
     * @param headers 自定义头信息
     * @param eventHandler SSE事件处理器
     */
    public CompletableFuture<Void> getSseWithHeaders(String url, Map<String, String> headers,
            HttpStreamEventInterface eventHandler) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            SseResponseHandler handler = new SseResponseHandler(eventHandler);
            BoundRequestBuilder requestBuilder = asyncHttpClient.prepareGet(url)
                    .setHeader("Accept", "text/event-stream")
                    .setHeader("Cache-Control", "no-cache");

            // 添加自定义头
            if (headers != null) {
                headers.forEach(requestBuilder::setHeader);
            }

            requestBuilder.execute(handler);
            future.complete(null);

        } catch (Exception e) {
            log.error("发起SSE请求失败: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    /**
     * 发送POST SSE请求
     * @param url 请求URL
     * @param body 请求体
     * @param eventHandler SSE事件处理器
     */
    public CompletableFuture<Void> postSse(String url, String body, Map<String, String> headers,
            HttpStreamEventInterface eventHandler) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            SseResponseHandler handler = new SseResponseHandler(eventHandler);

            BoundRequestBuilder requestBuilder = asyncHttpClient.preparePost(url)
                    .setHeader("Accept", "text/event-stream")
                    .setHeader("Cache-Control", "no-cache")
                    .setHeader("Content-Type", "application/json");

            if (headers != null) {
                headers.forEach(requestBuilder::setHeader);
            }

            requestBuilder.setBody(body).execute(handler);

            future.complete(null);

        } catch (Exception e) {
            log.error("发起POST SSE请求失败: {}", e.getMessage(), e);
            future.completeExceptionally(e);
        }

        return future;
    }

    /**
     * 简化的SSE事件监听器（流式处理数据）
     * @param url 请求URL
     * @param dataConsumer 数据消费者
     */
    public void listenSseData(String url, Consumer<String> dataConsumer) {
        HttpStreamEventInterface handler = new HttpStreamEventInterface() {
            @Override
            public void onEvent(String eventType, String data, String id) {
                if ("message".equals(eventType) && data != null && !data.isEmpty()) {
                    dataConsumer.accept(data);
                }
            }

            @Override
            public void onComment(String comment) {
                // 忽略注释
            }

            @Override
            public void onComplete() {
                log.info("SSE流完成");
            }

            @Override
            public void onError(Throwable t) {
                log.error("SSE流错误: {}", t.getMessage());
            }
        };

        getSseAsync(url, handler);
    }

    /**
     * 关闭客户端
     */
    public void close() {
        try {
            if (asyncHttpClient != null) {
                asyncHttpClient.close();
            }
        } catch (Exception e) {
            log.error("关闭SSE客户端失败: {}", e.getMessage(), e);
        }
    }

}
