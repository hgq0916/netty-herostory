package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.model.BroadCaster;
import org.tinygame.herostory.model.MoveState;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author hugangquan
 * @date 2021/11/09 22:29
 */
public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd>{

    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd msg) {
        //处理用户移动消息
        GameMsgProtocol.UserMoveToCmd userMoveToCmd = msg;

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if(userId == null){
            return;
        }


        User user = UserManager.getUserById(userId);

        if(user == null){
            return;
        }

        long startTime = System.currentTimeMillis();

        MoveState movestate = MoveState.builder()
                .fromPosX(userMoveToCmd.getMoveFromPosX())
                .fromPosY(userMoveToCmd.getMoveFromPosY())
                .startTime(startTime)
                .toPosX(userMoveToCmd.getMoveToPosX())
                .toPosY(userMoveToCmd.getMoveToPosY())
                .build();

        //记录用户当前位置
        user.setMoveState(movestate);

        GameMsgProtocol.UserMoveToResult.Builder builder = GameMsgProtocol.UserMoveToResult.newBuilder();
        builder.setMoveFromPosX(userMoveToCmd.getMoveFromPosX());
        builder.setMoveFromPosY(userMoveToCmd.getMoveFromPosY());
        builder.setMoveToPosX(userMoveToCmd.getMoveToPosX());
        builder.setMoveToPosY(userMoveToCmd.getMoveToPosY());
        builder.setMoveStartTime(startTime);
        builder.setMoveUserId(userId);

        GameMsgProtocol.UserMoveToResult userMoveToResult = builder.build();

        //群发用户移动消息
        BroadCaster.broadcast(userMoveToResult);
    }

}
