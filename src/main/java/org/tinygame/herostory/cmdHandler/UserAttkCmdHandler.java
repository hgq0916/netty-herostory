package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.model.BroadCaster;
import org.tinygame.herostory.msg.GameMsgProtocol;
import org.tinygame.herostory.msg.GameMsgProtocol.UserAttkCmd;

/**
 * 用户攻击消息处理
 * @author hugangquan
 * @date 2021/11/19 11:52
 */
public class UserAttkCmdHandler implements ICmdHandler<UserAttkCmd> {

    @Override
    public void handle(ChannelHandlerContext ctx, UserAttkCmd userAttkCmd) {

        //获取当前用户
        Integer attkUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if(attkUserId == null){
            return;
        }

        int targetUserId = userAttkCmd.getTargetUserId();

        //创建用户攻击消息
        GameMsgProtocol.UserAttkResult.Builder builder = GameMsgProtocol.UserAttkResult.newBuilder();
        builder.setAttkUserId(attkUserId);
        builder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserAttkResult userAttkResult = builder.build();

        //广播用户攻击消息
        BroadCaster.broadcast(userAttkResult);

    }
}
