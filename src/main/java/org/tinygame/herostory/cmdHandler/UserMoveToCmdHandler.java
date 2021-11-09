package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.model.BroadCaster;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author hugangquan
 * @date 2021/11/09 22:29
 */
public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd>{

    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd msg) {
        //处理用户移动消息
        GameMsgProtocol.UserMoveToCmd userMoveToCmd = msg;
        GameMsgProtocol.UserMoveToResult.Builder builder = GameMsgProtocol.UserMoveToResult.newBuilder();
        builder.setMoveToPosX(userMoveToCmd.getMoveToPosX());
        builder.setMoveToPosY(userMoveToCmd.getMoveToPosY());

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if(userId == null){
            return;
        }
        builder.setMoveUserId(userId);

        GameMsgProtocol.UserMoveToResult userMoveToResult = builder.build();

        //群发用户移动消息
        BroadCaster.broadcast(userMoveToResult);
    }

}
