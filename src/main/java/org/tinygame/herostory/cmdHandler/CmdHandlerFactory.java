package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;
import org.tinygame.herostory.util.PackageUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author hugangquan
 * @date 2021/11/09 22:45
 */
public final class CmdHandlerFactory {

    private static Logger logger = LoggerFactory.getLogger(CmdHandlerFactory.class);

    private static final Map<Class<?>,ICmdHandler> cmdHandlerMap = new HashMap<Class<?>, ICmdHandler>();

    public static void init() {
        String basePackage = ICmdHandler.class.getPackage().getName();
        Set<Class<?>> subClasses = PackageUtil.listSubClazz(basePackage, true, ICmdHandler.class);

        if(subClasses == null || subClasses.isEmpty()){
            return;
        }

        for(Class<?> clazz : subClasses){
            if(clazz.isInterface()){
                continue;
            }
            try {
                Method[] declaredMethods = clazz.getDeclaredMethods();

                if(declaredMethods == null || declaredMethods.length==0){
                    continue;
                }

                for(Method method : declaredMethods){
                    //获取参数类型
                    if(!method.getName().equals("handle")){
                        continue;
                    }

                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if(method.getParameterCount()!=2 || !GeneratedMessageV3.class.isAssignableFrom(parameterTypes[1])){
                        continue;
                    }

                    Class<?> parameterType = parameterTypes[1];

                    ICmdHandler handler = (ICmdHandler) clazz.newInstance();
                    cmdHandlerMap.put(parameterType,handler);
                }

            } catch (Exception e) {
                logger.error("",e);
            }
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
