package org.tinygame.herostory.model;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author hugangquan
 * @date 2021/11/09 22:18
 */
public final class BroadCaster {

    private BroadCaster(){}

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void addChannel(Channel channel){
        channelGroup.add(channel);
    }

    public static void removeChannel(Channel channel){
        channelGroup.remove(channel);
    }

    public static void broadcast(Object msg){
        channelGroup.writeAndFlush(msg);
    }

}
