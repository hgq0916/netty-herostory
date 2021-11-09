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
        GameMsgProtocol.UserEntryCmd userEntryCmd = msg;
        int userId = userEntryCmd.getUserId();
        String heroAvatar = userEntryCmd.getHeroAvatar();

        //向所有客户端转发用户入场消息
        GameMsgProtocol.UserEntryResult.Builder builder = GameMsgProtocol.UserEntryResult.newBuilder();
        builder.setUserId(userId);
        builder.setHeroAvatar(heroAvatar);
        GameMsgProtocol.UserEntryResult userEntryResult = builder.build();

        //将用户加入群组
        User user = User.builder().id(userId).heroAvatar(heroAvatar).build();
        UserManager.addUser(user);
        //将用户信息附着到channel上
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        BroadCaster.broadcast(userEntryResult);
    }

}
