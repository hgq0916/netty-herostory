package org.tinygame.herostory;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.tinygame.herostory.msg.GameMsgProtocol.MsgCode.*;

/**
 * 消息识别器
 * @author hugangquan
 * @date 2021/11/10 22:22
 */
public final class GameMsgRecognizer {


    private static Logger logger = LoggerFactory.getLogger(GameMsgRecognizer.class);

    private static final Map<Integer,Message.Builder> messageBuildMap = new HashMap<>();

    private static final Map<Class<?>,Integer>gameMsgCodeMap = new HashMap<>();

    static void init(){
        //获取protocol下定义的所有类
        Class<?>[] declaredClasses = GameMsgProtocol.class.getDeclaredClasses();

        for(Class<?> clazz : declaredClasses){
            //获取类名，并转换为小写
            String simpleName = clazz.getSimpleName();
            simpleName = simpleName.toLowerCase();
            for(GameMsgProtocol.MsgCode msgCode: GameMsgProtocol.MsgCode.values()){
                String msgName = msgCode.name();
                msgName = msgName.replaceAll("_","").toLowerCase();
                if(!msgName.startsWith(simpleName)){
                    continue;
                }

                try {
                    Object instance = clazz.getDeclaredMethod("getDefaultInstance").invoke(clazz);
                    Message.Builder builder = (Message.Builder) clazz.getDeclaredMethod("newBuilderForType").invoke(instance);
                    messageBuildMap.put(msgCode.getNumber(),builder);
                    gameMsgCodeMap.put(clazz,msgCode.getNumber());
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                }

            }
        }

    }

    private GameMsgRecognizer(){}

    public static Message.Builder getGameMsgBuilderByMsgCode(int msgCode){
        return messageBuildMap.get(msgCode);
    }

    public static int getGameMsgCodeByMsg(Class<?> msgClazz){
        if(null == msgClazz){
            return -1;
        }

        return gameMsgCodeMap.get(msgClazz);

    }

}
