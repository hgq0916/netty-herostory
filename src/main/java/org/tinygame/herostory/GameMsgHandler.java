package org.tinygame.herostory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author hugangquan
 * @date 2021/09/21 17:46
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {


    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息:"+msg);

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame)msg;
        ByteBuf content = frame.content();

        byte[] bytes = new byte[content.readableBytes()];
        content.readBytes(bytes);

        System.out.println("收到的字节:");
        for(byte b: bytes){
            System.out.print(b);
            System.out.print(",");
        }
        System.out.println();
    }
}
