package com.timecat.middle.block.im;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-04
 * @description null
 * @usage null
 */
public class IMExecutor {

    /**
     * 单任务队列
     */
    private static ExecutorService mExecutorSingle = Executors.newSingleThreadExecutor();
    /**
     * 线程池队列
     */
    private static ExecutorService mExecutorPool = Executors.newCachedThreadPool();


    /**
     * 异步多任务执行
     */
    public static void asyncMultiTask(Runnable runnable) {
        mExecutorPool.submit(runnable);
    }

    /**
     * 异步单任务执行
     */
    public static void asyncSingleTask(Runnable runnable) {
        mExecutorSingle.submit(runnable);
    }

}