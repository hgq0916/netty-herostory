package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;
import org.tinygame.herostory.msg.GameMsgProtocol.*;

/**
 * 定义消息编码器
 * @author hugangquan
 * @date 2021/11/07 21:31
 */
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(GameMsgEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        //判断消息类型是否是游戏对象
        if(!(msg instanceof GeneratedMessageV3)){
            super.write(ctx,msg,promise);
            return;
        }

        int msgCode = GameMsgRecognizer.getGameMsgCodeByMsg(msg.getClass());

        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeShort(0);
        buffer.writeShort(msgCode);
        buffer.writeBytes(((GeneratedMessageV3) msg).toByteArray());

        BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(buffer);

        ctx.writeAndFlush(binaryWebSocketFrame,promise);

    }
}
