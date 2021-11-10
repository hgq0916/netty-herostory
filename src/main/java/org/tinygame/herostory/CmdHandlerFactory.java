package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.ICmdHandler;
import org.tinygame.herostory.cmdHandler.UserEntryCmdHandler;
import org.tinygame.herostory.cmdHandler.UserMoveToCmdHandler;
import org.tinygame.herostory.cmdHandler.WhoElseIsHereCmdHandler;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hugangquan
 * @date 2021/11/09 22:45
 */
public final class CmdHandlerFactory {

    private static Logger log = LoggerFactory.getLogger(CmdHandlerFactory.class);

    private static final Map<Class<?>,ICmdHandler> cmdHandlerMap = new HashMap<Class<?>, ICmdHandler>();

    static void init(){
        cmdHandlerMap.put(GameMsgProtocol.UserEntryCmd.class,new UserEntryCmdHandler());
        cmdHandlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHandler());
        cmdHandlerMap.put(GameMsgProtocol.UserMoveToCmd.class,new UserMoveToCmdHandler());
    }

    private CmdHandlerFactory(){}


    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> clazz){
       if(clazz == null){
           return null;
       }

       return cmdHandlerMap.get(clazz);
    }
}
