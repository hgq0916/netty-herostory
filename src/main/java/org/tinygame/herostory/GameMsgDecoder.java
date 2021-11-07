package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.Arrays;

/**
 * 消息解码器
 * @author hugangquan
 * @date 2021/11/07 21:00
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame)msg;
        ByteBuf content = frame.content();

        //读取消息长度
        content.readShort();

        //读取消息编号
        int msgNum = content.readShort();

        //消息体
        byte[] msgBody = new byte[content.readableBytes()];
        content.readBytes(msgBody);

        GameMsgProtocol.MsgCode msgCode = GameMsgProtocol.MsgCode.forNumber(msgNum);

        GeneratedMessageV3 generatedMessageV3 = null;

        switch (msgCode){

            case USER_ENTRY_CMD:
                generatedMessageV3 = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
                break;
            case WHO_ELSE_IS_HERE_CMD:
                generatedMessageV3 = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                break;
            case USER_MOVE_TO_CMD:
                generatedMessageV3 = GameMsgProtocol.UserMoveToCmd.parseFrom(msgBody);
                break;
            case USER_STOP_CMD:
                generatedMessageV3 = GameMsgProtocol.UserStopCmd.parseFrom(msgBody);
                break;
            case USER_ATTK_CMD:
                generatedMessageV3 = GameMsgProtocol.UserAttkCmd.parseFrom(msgBody);
                break;
            default:
                throw new RuntimeException("不支持的消息编码:"+msgCode);
        }

        //重新将消息放到通道
        ctx.fireChannelRead(generatedMessageV3);

    }
}
