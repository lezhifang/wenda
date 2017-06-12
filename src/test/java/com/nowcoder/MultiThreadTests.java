package com.nowcoder;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LZF on 2017/6/12.
 */

class MyThread extends Thread{

    private int tid;
    public MyThread(int tid){
        this.tid = tid;
    }

    @Override
    public void run() {
        try{
            for(int i = 0; i < 10; i++){
                Thread.sleep(1000);//睡眠1s
                System.out.println(String.format("%d:%d",tid,i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
public class MultiThreadTests {
    public  void testThread(){
        for(int i = 0; i < 10; i++){
            //new MyThread(i).start();
        }

        for(int i = 0; i < 10; i++){
             int t = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        for(int j = 0; j < 10; j++){
                            Thread.sleep(1000);//睡眠1s
                            System.out.println(String.format("T2 %d: %d",t,j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static Object obj = new Object();
    public static void testSynchronized1(){
        synchronized(obj){
            try{
                for(int i = 0; i < 10; i++){
                    Thread.sleep(1000);//睡眠1s
                    System.out.println(String.format("T3 %d ",i));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void testSynchronized2(){
        synchronized(new Object()){
            try{
                for(int i = 0; i < 10; i++){
                    Thread.sleep(1000);//睡眠1s
                    System.out.println(String.format("T4 %d ",i));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized(){
        for(int i = 0; i < 10; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

     static class Consumer implements Runnable{
        private BlockingQueue<String> queue;
        public Consumer(BlockingQueue<String> queue){
            this.queue = queue;
        }
        @Override
        public void run() {
            try{
                while (true){
                    System.out.println(Thread.currentThread().getName() + ":" + queue.take());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    static class Producer implements Runnable{
        private BlockingQueue<String> queue;
        public Producer(BlockingQueue<String> queue){
            this.queue = queue;
        }
        @Override
        public void run() {
            try{
                for(int i = 0; i < 100; i++){
                    Thread.sleep(1000);
                    queue.put(String.valueOf(i));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public  static void testBlockingQueue(){
        //这两个操作是：获取元素时等待队列变为非空，以及存储元素时等待空间变得可用
        BlockingQueue<String> queue = new ArrayBlockingQueue<String>(12);
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue),"Consumer1").start();
        new Thread(new Consumer(queue),"Consumer2").start();
    }

    private static ThreadLocal<Integer> threadLocalUserId = new ThreadLocal<Integer>();
    private static Integer userId;
    private static void testThreadLocal(){
        for(int i = 0; i < 10; i++){
            int t = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        threadLocalUserId.set(t);//线程局部变量  线程私有
                        Thread.sleep(1000);
                        System.out.println("ThreadLocal:" + threadLocalUserId.get());
//                        userId = t; //线程共享变量
//                        Thread.sleep(1000);
//                        System.out.println("UserId: " + userId);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testExecutor(){
        //ExecutorService service = Executors.newSingleThreadExecutor();
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    try{
                        Thread.sleep(1000);
                        System.out.println("Executor1: " + i);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < 10; i++){
                    try{
                        Thread.sleep(1000);
                        System.out.println("Executor2: " + i);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        service.shutdown();//启动一次顺序关闭，执行以前提交的任务，但不接受新任务。如果已经关闭，则调用没有其他作用
        //如果关闭后所有任务都已完成，则返回 true。注意，除非首先调用 shutdown 或 shutdownNow，否则 isTerminated 永不为 true。
        while(!service.isTerminated()){
            try{
                Thread.sleep(1000);
                System.out.println("wait for termination");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static int count = 0;
    public static AtomicInteger  atomicInteger = new AtomicInteger(0);

    private static void testWithoutAtomic(){
        for(int i = 0; i < 10; ++i){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(1000);
                        for(int j = 0; j < 10; j++){
                            count++;
                            System.out.println(count);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static void testWithAtomic(){
        for(int i = 0; i < 10; ++i){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        Thread.sleep(1000);
                        for(int j = 0; j < 10; j++){
                            System.out.println(atomicInteger.incrementAndGet());
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testFuture(){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {//返回异步结果
            @Override
            public Integer call() throws Exception {
                //Thread.sleep(1000);
                throw new IllegalArgumentException("异常");//可以通过future.get()方法获取线程中的Exception
                //return 1;
            }
        });

        service.shutdown();
        try{
            System.out.println(future.get());//阻塞等待返回结果
            //System.out.println(future.get(100, TimeUnit.MILLISECONDS));//timeout   此处报错：java.util.concurrent.TimeoutException
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        //testThread();
        //testSynchronized();
        //testBlockingQueue();
        //testThreadLocal();
        //testExecutor();
        //testWithoutAtomic();
        testFuture();
    }
}
