package org.tinygame.herostory;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

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
        messageBuildMap.put(USER_ENTRY_CMD_VALUE,GameMsgProtocol.UserEntryCmd.newBuilder());
        messageBuildMap.put(WHO_ELSE_IS_HERE_CMD_VALUE,GameMsgProtocol.WhoElseIsHereCmd.newBuilder());
        messageBuildMap.put(USER_MOVE_TO_CMD_VALUE,GameMsgProtocol.UserMoveToCmd.newBuilder());
        messageBuildMap.put(USER_STOP_CMD_VALUE,GameMsgProtocol.UserStopCmd.newBuilder());
        messageBuildMap.put(USER_ATTK_CMD_VALUE,GameMsgProtocol.UserAttkCmd.newBuilder());

        gameMsgCodeMap.put(GameMsgProtocol.UserEntryResult.class,GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
        gameMsgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class,GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
        gameMsgCodeMap.put(GameMsgProtocol.UserMoveToResult.class,GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
        gameMsgCodeMap.put(GameMsgProtocol.UserQuitResult.class,GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);
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
