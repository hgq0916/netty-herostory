package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;
import org.tinygame.herostory.cmdHandler.ICmdHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 主线程处理器
 * @author hugangquan
 * @date 2021/11/20 20:38
 */
public final class MainTheadProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(MainTheadProcessor.class);

    private final static MainTheadProcessor mainThreadProcessor = new MainTheadProcessor();

    private ExecutorService executorService;

    private void init(){
        executorService = Executors.newSingleThreadExecutor((r)->
            new Thread(r,"MainTheadProcessor")
        );
    }

    private MainTheadProcessor(){
        init();
    }

    public static MainTheadProcessor getInstance(){
        return mainThreadProcessor;
    }

    public void process(ChannelHandlerContext ctx, GeneratedMessageV3 msg){

        executorService.submit(()->{

            System.out.println("当前执行线程："+Thread.currentThread().getName());

            ICmdHandler<? extends GeneratedMessageV3> handler = CmdHandlerFactory.create(msg.getClass());
            if(handler != null){
                //泛型处理
                try{
                    handler.handle(ctx,cast(msg));
                }catch (Exception ex){
                    LOGGER.error("",ex);
                }
            }
        });
    }

    static private  <TCmd extends GeneratedMessageV3> TCmd cast(Object msg){
        if(msg == null){
            return null;
        }
        return (TCmd)msg;
    }


}
