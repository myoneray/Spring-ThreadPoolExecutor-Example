package com.myoneray.git;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
// 指定的运行runner，并且把你所指定的Runner作为参数传递给它
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class TestThreadPool extends AbstractJUnit4SpringContextTests {

    private static int produceTaskSleepTime = 10;

    private static int produceTaskMaxNumber = 100;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    // newFixedThreadPool就是一个固定大小的ThreadPool
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    // newCachedThreadPool比较适合没有固定大小并且比较快速就能完成的小任务，没必要维持一个Pool，
    // 这比直接new Thread来处理的好处是能在60秒内重用已创建的线程。
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return threadPoolTaskExecutor;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Test
    public void testThreadPoolExecutor() {
        for (int i = 1; i <= produceTaskMaxNumber; i++) {
            try {
                Thread.sleep(produceTaskSleepTime);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            new Thread(new StartTaskThread(threadPoolTaskExecutor, i)).start();
        }

    }

}