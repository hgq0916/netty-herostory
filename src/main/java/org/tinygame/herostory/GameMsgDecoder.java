package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.Arrays;

/**
 * 消息解码器
 * @author hugangquan
 * @date 2021/11/07 21:00
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(GameMsgDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame)msg;
        ByteBuf content = frame.content();

        //读取消息长度
        content.readShort();

        //读取消息编号
        int msgCode = content.readShort();

        Message.Builder builder = GameMsgRecognizer.getGameMsgBuilderByMsgCode(msgCode);

        if(null == builder){
            logger.error("不支持的消息编码:"+msgCode);
            return;
        }

        //消息体
        byte[] msgBody = new byte[content.readableBytes()];
        content.readBytes(msgBody);

        builder.clear();
        Message message = builder.build().getParserForType().parseFrom(msgBody);

        //重新将消息放到通道
        ctx.fireChannelRead(message);
    }
}
