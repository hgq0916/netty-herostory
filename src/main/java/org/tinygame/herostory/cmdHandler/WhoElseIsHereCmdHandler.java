package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @author hugangquan
 * @date 2021/11/09 22:28
 */
public class WhoElseIsHereCmdHandler  implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{

    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd msg) {
        //都有哪些用户在场
        GameMsgProtocol.WhoElseIsHereResult.Builder builder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        for(User user : UserManager.userList()){
            if(user == null){
                continue;
            }
            Integer userId = user.getId();
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userBuilder.setUserId(userId);
            userBuilder.setHeroAvatar(user.getHeroAvatar());
            GameMsgProtocol.WhoElseIsHereResult.UserInfo userInfo = userBuilder.build();
            builder.addUserInfo(userInfo);
        }
        GameMsgProtocol.WhoElseIsHereResult whoElseIsHereResult = builder.build();

        ctx.writeAndFlush(whoElseIsHereResult);
    }
}
