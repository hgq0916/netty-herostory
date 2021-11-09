package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.msg.GameMsgProtocol;
import org.tinygame.herostory.msg.GameMsgProtocol.*;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *
 * @author hugangquan
 * @date 2021/09/21 17:46
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static Logger log = LoggerFactory.getLogger(GameMsgHandler.class);

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final Map<Integer, User> userMap = new ConcurrentHashMap<Integer, User>();

    /*
        通道激活
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //将新的客户端加入通道组
        super.channelActive(ctx);
        channelGroup.add(ctx.channel());
    }


    /***
     *
     * 通道断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        channelGroup.remove(ctx.channel());
        //从用户组中移除
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        userMap.remove(userId);

        //通知其他用户该用户离场
        UserQuitResult.Builder builder = UserQuitResult.newBuilder();
        builder.setQuitUserId(userId);
        UserQuitResult userQuitResult = builder.build();
        channelGroup.writeAndFlush(userQuitResult);
    }

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息:clazz="+msg.getClass().getName()+",msg="+msg);

        //判断消息是否属于游戏消息，不属于则不处理
        if(!(msg instanceof GeneratedMessageV3)){
            return;
        }

        if(msg instanceof UserEntryCmd){
            UserEntryCmd userEntryCmd = (UserEntryCmd) msg;
            int userId = userEntryCmd.getUserId();
            String heroAvatar = userEntryCmd.getHeroAvatar();

            //向所有客户端转发用户入场消息
            UserEntryResult.Builder builder = UserEntryResult.newBuilder();
            builder.setUserId(userId);
            builder.setHeroAvatar(heroAvatar);
            UserEntryResult userEntryResult = builder.build();

            //将用户加入群组
            User user = User.builder().id(userId).heroAvatar(heroAvatar).build();
            userMap.put(userId,user);
            //将用户信息附着到channel上
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

            channelGroup.writeAndFlush(userEntryResult);
        }else  if(msg instanceof WhoElseIsHereCmd){
            //都有哪些用户在场
            WhoElseIsHereResult.Builder builder = WhoElseIsHereResult.newBuilder();

            for(Map.Entry<Integer,User> userEntry: userMap.entrySet()){
                if(userEntry.getKey() == null || userEntry.getValue() == null){
                    continue;
                }
                Integer userId = userEntry.getKey();
                User user = userEntry.getValue();
                WhoElseIsHereResult.UserInfo.Builder userBuilder =WhoElseIsHereResult.UserInfo.newBuilder();
                userBuilder.setUserId(userId);
                userBuilder.setHeroAvatar(user.getHeroAvatar());
                WhoElseIsHereResult.UserInfo userInfo = userBuilder.build();
                builder.addUserInfo(userInfo);
            }
            WhoElseIsHereResult whoElseIsHereResult = builder.build();

            ctx.writeAndFlush(whoElseIsHereResult);
        }else  if(msg instanceof UserMoveToCmd){
            //处理用户移动消息
            UserMoveToCmd userMoveToCmd = (UserMoveToCmd) msg;
            UserMoveToResult.Builder builder = UserMoveToResult.newBuilder();
            builder.setMoveToPosX(userMoveToCmd.getMoveToPosX());
            builder.setMoveToPosY(userMoveToCmd.getMoveToPosY());

            Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

            if(userId == null){
                return;
            }
            builder.setMoveUserId(userId);

            UserMoveToResult userMoveToResult = builder.build();

            //群发用户移动消息
            channelGroup.writeAndFlush(userMoveToResult);

        }else {
            log.error("不支持的消息类型，clazz:"+msg.getClass().getName());
            return;
        }

    }
}
