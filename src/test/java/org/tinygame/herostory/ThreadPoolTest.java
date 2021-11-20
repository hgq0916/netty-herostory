package org.tinygame.herostory;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 线程池测试
 * @author hugangquan
 * @date 2021/11/20 20:50
 */
public class ThreadPoolTest {

    /**
     * 测试阻塞队列
     */
    @Test
    public void test01(){
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();

        //创建两个生产者线程，一个消费者线程

        Thread t1 = new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                blockingQueue.offer(i);
            }
        });

        Thread t2 = new Thread(()->{
            for(int i=10;i<20;i++){
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                blockingQueue.offer(i);
            }
        });

        Thread t3 = new Thread(()->{
            while (true){
                try {
                    Integer i = blockingQueue.take();
                    System.out.println("从队列中取值："+i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试阻塞队列
     */
    @Test
    public void test02(){

        //创建两个生产者线程，一个消费者线程

        Thread t1 = new Thread(()->{
            for(int i=0;i<10;i++){
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int finalI = i;
                MyExecutorService.getInstance().submit(()->{
                    System.out.println("当前值:"+ finalI);
                });
            }
        });

        Thread t2 = new Thread(()->{
            for(int i=10;i<20;i++){
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int finalI = i;
                MyExecutorService.getInstance().submit(()->{
                    System.out.println("当前值:"+ finalI);
                });
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

/**
 * 自定义线程池
 */
class MyExecutorService {

    private static MyExecutorService instance = new MyExecutorService();

    private final static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

    private  static Thread thread;

    private MyExecutorService(){
        thread = new Thread(()->{

            while (true){
                try {
                    Runnable runnable = queue.take();
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        thread.start();

    }

    public static MyExecutorService getInstance(){
        return instance;
    }

    public void submit(Runnable runnable){
        queue.offer(runnable);
    }

}
