package com.ddhigh.halia.chat.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void submit(Runnable r) {
        pool.submit(r);
    }
}
