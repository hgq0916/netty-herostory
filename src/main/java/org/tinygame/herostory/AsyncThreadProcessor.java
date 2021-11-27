package org.tinygame.herostory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步线程处理器
 * @author hugangquan
 * @date 2021/11/23 22:02
 */
public final class AsyncThreadProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncThreadProcessor.class);

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

    /**
     * 获取随机线程
     * @return
     */
    private ExecutorService getRandomExecutorService(){

        if(executorServices == null || executorServices.length == 0) {
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(executorServices.length);

        return executorServices[index];
    }

    private ExecutorService getExecutorService(int bindId){
        //根据bindId找到一个线程池
        int hash = Math.abs(bindId);
        int index = hash % executorServices.length;

        return executorServices[index];
    }

    public void process(AsyncOperation asyncOperation,int bindId){

        ExecutorService executorService = getExecutorService(bindId);

        process(executorService,asyncOperation);
    }

    private void process(ExecutorService executorService,AsyncOperation asyncOperation){
        executorService.submit(()->{
            try{
                asyncOperation.doAsync();
                MainTheadProcessor.getInstance().process(()->{
                    try{
                        asyncOperation.doFinish();
                    }catch (Exception ex){
                        LOGGER.error("",ex);
                    }
                });
            }catch (Exception ex){
                LOGGER.error("",ex);
            }

        });
    }

    public void process(AsyncOperation asyncOperation) {

        if(asyncOperation == null){
            return;
        }

        ExecutorService randomExecutorService = getRandomExecutorService();

        process(randomExecutorService,asyncOperation);

    }

}
