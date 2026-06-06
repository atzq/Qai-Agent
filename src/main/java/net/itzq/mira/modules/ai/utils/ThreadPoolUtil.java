package net.itzq.mira.modules.ai.utils;

import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @discription
 *
 * @created 2026/3/8 16:55
 */
public class ThreadPoolUtil {

    private static final ExecutorService pool = Executors.newFixedThreadPool(128);

    // 使用TtlExecutors工具类包装原始的线程池，使其兼容TransmittableThreadLocal
    private static final ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(pool);

    public static ExecutorService getExecutorService() {
        return ttlExecutorService;
    }
}
