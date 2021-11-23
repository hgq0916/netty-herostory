package org.tinygame.herostory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步线程处理器
 * @author hugangquan
 * @date 2021/11/23 22:02
 */
public final class AsyncThreadProcessor {

    private  final ExecutorService[] executorServices;

    private AsyncThreadProcessor(){

        executorServices = new ExecutorService[8];

       for(int i=0;i<executorServices.length;i++){
           int finalI = i;
           executorServices[i] = Executors.newSingleThreadExecutor((r)->{
               return new Thread(r,"AsyncThreadProcessor_"+ finalI);
           });
       }
    }

    private static final AsyncThreadProcessor instance = new AsyncThreadProcessor();

    public static AsyncThreadProcessor getInstance(){
        return instance;
    }

    public void process(Runnable runnable,int bindId){

        //根据bindId找到一个线程池
        int hash = Math.abs(bindId);
        int index = hash % executorServices.length;

        executorServices[index].submit(runnable);

    }

}
