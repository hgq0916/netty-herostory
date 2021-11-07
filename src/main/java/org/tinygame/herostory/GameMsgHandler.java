package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol.UserEntryCmd;
import org.tinygame.herostory.msg.GameMsgProtocol.UserEntryResult;


/**
 *
 * @author hugangquan
 * @date 2021/09/21 17:46
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static Logger log = LoggerFactory.getLogger(GameMsgHandler.class);

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //将新的客户端加入通道组
        channelGroup.add(ctx.channel());
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

            channelGroup.writeAndFlush(userEntryResult);
        }else {
            log.error("不支持的消息类型，clazz:"+msg.getClass().getName());
            return;
        }

    }
}
