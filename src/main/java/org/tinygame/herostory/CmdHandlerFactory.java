package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.ICmdHandler;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hugangquan
 * @date 2021/11/09 22:45
 */
public final class CmdHandlerFactory {

    private static Logger logger = LoggerFactory.getLogger(CmdHandlerFactory.class);

    private static final Map<Class<?>,ICmdHandler> cmdHandlerMap = new HashMap<Class<?>, ICmdHandler>();

    static void init(){

        String cmdHandlerFullClassName = ICmdHandler.class.getName();
        String basePackage = cmdHandlerFullClassName.substring(0, cmdHandlerFullClassName.lastIndexOf("."));

        Class<?>[] declaredClasses = GameMsgProtocol.class.getDeclaredClasses();

        for(Class<?> clazz : declaredClasses){
            //判断GeneratedMessageV3是否是class是的父类
            if(!GeneratedMessageV3.class.isAssignableFrom(clazz)){
                continue;
            }
            String simpleName = clazz.getSimpleName();

            String handlerClazzName = basePackage +"."+simpleName+"Handler";
            Class<?> handlerClazz = null;
            try {
                handlerClazz = ICmdHandler.class.getClassLoader().loadClass(handlerClazzName);
            } catch (Exception e) {
               logger.error("加载该消息对应的处理器失败，clazz:"+clazz.getSimpleName());
            }

            if(handlerClazz == null){
                logger.error("加载该消息对应的处理器失败，clazz:"+clazz.getSimpleName());
                continue;
            }

            //判断ICmdHandler是否是该类的父类
            if(!ICmdHandler.class.isAssignableFrom(handlerClazz)){
                continue;
            }
            ICmdHandler handler = null;
            try {
                handler = (ICmdHandler) handlerClazz.newInstance();
            } catch (Exception e) {
                logger.error("",e);
            }
            cmdHandlerMap.put(clazz,handler);
        }
    }

    private CmdHandlerFactory(){}


    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> clazz){
       if(clazz == null){
           return null;
       }

       return cmdHandlerMap.get(clazz);
    }
}
