package com.myoneray.git;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class StartTaskThread implements Runnable {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private int i;

    // 1）当池子大小小于corePoolSize就新建线程，并处理请求
    // 2）当池子大小等于corePoolSize，把请求放入workQueue中，池子里的空闲线程就去从workQueue中取任务并处理
    // 3）当workQueue放不下新入的任务时，新建线程入池，并处理请求，如果池子大小撑到了maximumPoolSize就用RejectedExecutionHandler来做拒绝处理
    // 4）另外，当池子的线程数大于corePoolSize的时候，多余的线程会等待keepAliveTime长的时间，如果无请求可处理就自行销毁

    public StartTaskThread(ThreadPoolTaskExecutor threadPoolTaskExecutor, int i) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.i = i;
    }

    public synchronized void run() {
        String task = "task@ " + i;
        System.out.println("创建任务并提交到线程池中：" + task);
        FutureTask<String> futureTask = new FutureTask<String>(new ThreadPoolTask(task));
        threadPoolTaskExecutor.execute(futureTask);
        // 在这里可以做别的任何事情
        String result = null;
        try {
            // 取得结果，同时设置超时执行时间为1秒。同样可以用future.get()，不设置执行超时时间取得结果
            result = futureTask.get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            futureTask.cancel(true);
        } catch (ExecutionException e) {
            futureTask.cancel(true);
        } catch (Exception e) {
            futureTask.cancel(true);
            // 超时后，进行相应处理
        } finally {
            System.out.println("task@" + i + ":result=" + result);
        }

    }
}