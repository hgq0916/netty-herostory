package org.tinygame.herostory.cmdHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.login.UserService;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserEntity;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;
import org.tinygame.herostory.msg.GameMsgProtocol.UserLoginCmd;

import java.util.function.Function;

/**
 * 用户登录消息处理
 * @author hugangquan
 * @date 2021/11/20 22:51
 */
public class UserLoginCmdHandler implements ICmdHandler<UserLoginCmd> {

    private final Logger LOGGER = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, UserLoginCmd userLoginCmd) {

        String userName = userLoginCmd.getUserName();

        String password = userLoginCmd.getPassword();

        if(userName == null || password == null){
            return;
        }

        UserService userService = UserService.getInstance();


        Function<UserEntity,Void> callback = (userEntity)->{
            try{

                if(userEntity == null){
                    LOGGER.error("用户登录失败，username="+userName);
                    return null;
                }

                //将用户绑定到通道
                ctx.channel().attr(AttributeKey.valueOf("userId")).set(userEntity.getId());

                //将用户加入群组
                User user = User.builder()
                        .id(userEntity.getId())
                        .username(userEntity.getUsername())
                        .heroAvatar(userEntity.getHeroAvatar())
                        .currentHp(User.INIT_HP)
                        .build();

                UserManager.addUser(user);

                //返回用户登录成功消息
                GameMsgProtocol.UserLoginResult.Builder builder = GameMsgProtocol.UserLoginResult.newBuilder();
                builder.setUserId(user.getId());
                builder.setUserName(user.getUsername());
                builder.setHeroAvatar(user.getHeroAvatar());

                GameMsgProtocol.UserLoginResult userLoginResult = builder.build();

                ctx.writeAndFlush(userLoginResult);

            }catch (Exception ex){
                LOGGER.error("",ex);
            }

            return null;

        };

        //用户登录
        userService.login(userName, password,callback);


    }
}
