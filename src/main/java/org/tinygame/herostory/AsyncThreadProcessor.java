package org.tinygame.herostory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步线程处理器
 * @author hugangquan
 * @date 2021/11/23 22:02
 */
public final class AsyncThreadProcessor {

    private  final ExecutorService executorService;

    private AsyncThreadProcessor(){
        executorService = Executors.newSingleThreadExecutor((r)->{
            return new Thread(r,"AsyncThreadProcessor");
        });
    }

    private static final AsyncThreadProcessor instance = new AsyncThreadProcessor();

    public static AsyncThreadProcessor getInstance(){
        return instance;
    }

    public void process(Runnable runnable){
        executorService.submit(runnable);
    }

}
