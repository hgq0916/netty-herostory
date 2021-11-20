package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.model.BroadCaster;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author hugangquan
 * @date 2021/11/09 22:17
 */
public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd>{

    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd msg) {

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if(userId == null){
            return;
        }

        User user = UserManager.getUserById(userId);
        if(user == null){
            return;
        }

        //向所有客户端转发用户入场消息
        GameMsgProtocol.UserEntryResult.Builder builder = GameMsgProtocol.UserEntryResult.newBuilder();
        builder.setUserId(userId);
        builder.setHeroAvatar(user.getHeroAvatar());
        builder.setUserName(user.getUsername());
        GameMsgProtocol.UserEntryResult userEntryResult = builder.build();

        BroadCaster.broadcast(userEntryResult);
    }

}
