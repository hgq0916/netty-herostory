package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.login.UserService;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;
import org.tinygame.herostory.msg.GameMsgProtocol.SelectHeroCmd;

/**
 * 选择英雄
 * @author hugangquan
 * @date 2021/11/21 10:39
 */
public class SelectHeroCmdHandler implements ICmdHandler<SelectHeroCmd> {
    @Override
    public void handle(ChannelHandlerContext ctx, SelectHeroCmd selectHeroCmd) {
        String heroAvatar = selectHeroCmd.getHeroAvatar();

        //获取当前用户id
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if(userId == null){
            return;
        }

        User user = UserManager.getUserById(userId);

        if(user == null){
            return;
        }

        user.setHeroAvatar(heroAvatar);

        //更新数据库的英雄形象
        UserService.getInstance().updateHeroAvatar(userId,heroAvatar);

        //响应选择英雄头像命令

        GameMsgProtocol.SelectHeroResult.Builder builder = GameMsgProtocol.SelectHeroResult.newBuilder();
        builder.setHeroAvatar(heroAvatar);
        GameMsgProtocol.SelectHeroResult selectHeroResult = builder.build();

        ctx.writeAndFlush(selectHeroResult);
    }
}
