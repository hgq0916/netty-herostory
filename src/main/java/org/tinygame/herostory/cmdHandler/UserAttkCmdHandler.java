package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.model.BroadCaster;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
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

        System.out.println(Thread.currentThread().getName());

        int targetUserId = userAttkCmd.getTargetUserId();

        //创建用户攻击消息
        GameMsgProtocol.UserAttkResult.Builder builder = GameMsgProtocol.UserAttkResult.newBuilder();
        builder.setAttkUserId(attkUserId);
        builder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserAttkResult userAttkResult = builder.build();

        //广播用户攻击消息
        BroadCaster.broadcast(userAttkResult);

        //用户攻击后，目标用户血量减少
        User targetUser = UserManager.getUserById(targetUserId);

        if(targetUser == null){
            return;
        }

        if(targetUser.getCurrentHp() <=0){
            return;
        }

        int subtractHp = 10;
        int currentHp = targetUser.getCurrentHp() - subtractHp;

        if(currentHp<=0){
            //用户死亡，广播用户死亡消息
            broadCastUserDied(targetUser);
        }

        targetUser.setCurrentHp(currentHp);

        //广播用户血量减少消息
        broadCastSubstractHp(targetUser,subtractHp);

    }

    /**
     * 广播用户死亡消息
     * @param targetUser
     */
    private void broadCastUserDied(User targetUser) {
        GameMsgProtocol.UserDieResult.Builder builder = GameMsgProtocol.UserDieResult.newBuilder();
        builder.setTargetUserId(targetUser.getId());
        GameMsgProtocol.UserDieResult userDieResult = builder.build();

        BroadCaster.broadcast(userDieResult);
    }

    /**
     * 广播用户血量减少消息
     * @param targetUser
     * @param subtractHp
     */
    private void broadCastSubstractHp(User targetUser, int subtractHp) {
        GameMsgProtocol.UserSubtractHpResult.Builder builder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        builder.setTargetUserId(targetUser.getId());
        builder.setSubtractHp(subtractHp);
        GameMsgProtocol.UserSubtractHpResult userSubtractHpResult = builder.build();

        BroadCaster.broadcast(userSubtractHpResult);

    }
}
