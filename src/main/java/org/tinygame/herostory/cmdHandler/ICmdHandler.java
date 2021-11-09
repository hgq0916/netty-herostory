package org.tinygame.herostory.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * 命令消息处理器接口
 * @author hugangquan
 * @date 2021/11/09 22:30
 */
public interface ICmdHandler<Cmd extends GeneratedMessageV3> {

    void handle(ChannelHandlerContext ctx,Cmd cmd);

}
