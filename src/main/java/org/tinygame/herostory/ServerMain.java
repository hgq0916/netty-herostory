package org.tinygame.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.tinygame.herostory.cmdHandler.CmdHandlerFactory;
import org.tinygame.herostory.mq.RocketMqProducer;
import org.tinygame.herostory.util.RedisUtil;

/**
 * @author hugangquan
 * @date 2021/09/21 17:12
 */
public class ServerMain {

    public static void main(String[] args) {

        //初始化redis
        RedisUtil.init();
        //初始化mq生产者
        RocketMqProducer.init();
        //初始化数据库连接
        SqlSessionHolder.init();

        //初始化消息识别器
        GameMsgRecognizer.init();
        //初始化工厂
        CmdHandlerFactory.init();
        //两个线程池
        //负责处理客户端连接,获取连接后把连接交给worker线程池处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //负责读写客户端的消息
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup);

        //绑定处理客户端连接类
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                sc.pipeline().addLast(new HttpServerCodec(),
                        new HttpObjectAggregator(65535),
                        new WebSocketServerProtocolHandler("/websocket"),
                        new GameMsgEncoder(),
                        new GameMsgDecoder(),
                        new GameMsgHandler()
                );
            }
        });

        try{
            ChannelFuture cf =  bootstrap.bind(12345).sync();

            if(cf.isSuccess()){
                System.out.println("服务器启动成功");
            }
            cf.channel().closeFuture().sync();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
