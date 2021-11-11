package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdHandler.ICmdHandler;
import org.tinygame.herostory.model.BroadCaster;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol.UserQuitResult;


/**
 * 测试地址 http://cdn0001.afrxvk.cn/hero_story/demo/step010/index.html?serverAddr=127.0.0.1:12345&userId=1
 * @author hugangquan
 * @date 2021/09/21 17:46
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static Logger log = LoggerFactory.getLogger(GameMsgHandler.class);

    /*
        通道激活
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //将新的客户端加入通道组
        super.channelActive(ctx);
        BroadCaster.addChannel(ctx.channel());
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
        BroadCaster.removeChannel(ctx.channel());
        //从用户组中移除
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if(null == userId){
            return;
        }

        UserManager.removeUser(userId);

        //通知其他用户该用户离场
        UserQuitResult.Builder builder = UserQuitResult.newBuilder();
        builder.setQuitUserId(userId);
        UserQuitResult userQuitResult = builder.build();
        BroadCaster.broadcast(userQuitResult);
    }

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息:clazz="+msg.getClass().getName()+",msg="+msg);

        //判断消息是否属于游戏消息，不属于则不处理
        if(!(msg instanceof GeneratedMessageV3)){
            return;
        }

        ICmdHandler<? extends GeneratedMessageV3> handler = CmdHandlerFactory.create(msg.getClass());
        if(handler != null){
            //泛型处理
            handler.handle(ctx,cast(msg));
        }

    }


    static private  <TCmd extends GeneratedMessageV3> TCmd cast(Object msg){
        if(msg == null){
            return null;
        }
        return (TCmd)msg;
    }


}
