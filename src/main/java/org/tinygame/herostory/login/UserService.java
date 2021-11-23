package org.tinygame.herostory.login;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.AsyncThreadProcessor;
import org.tinygame.herostory.SqlSessionHolder;
import org.tinygame.herostory.login.mapper.UserMapper;
import org.tinygame.herostory.model.UserEntity;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author hugangquan
 * @date 2021/11/20 21:51
 */
public final class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserService(){}

    private static final UserService userService = new UserService();

    public static UserService getInstance(){
        return userService;
    }


    public void login(String username, String password, Function<UserEntity,Void> callback){

        AsyncThreadProcessor.getInstance().process(()->{

            LOGGER.info("当前线程："+Thread.currentThread().getName());

            if(null == username || null == password){
                callback.apply(null);
            }

            UserEntity userEntity = null;

            try(SqlSession sqlSession = SqlSessionHolder.getInstance().openSession()){
                UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

                userEntity = userMapper.getUserByUsername(username);

                if(userEntity == null){
                    //新建用户
                    userEntity = new UserEntity();
                    userEntity.setUsername(username);
                    userEntity.setPassword(password);
                    userEntity.setHeroAvatar("");
                    userMapper.insertUser(userEntity);
                }

                callback.apply(userEntity);

            }catch (Exception e){
                LOGGER.error("",e);
            }
        });
    }

    public void updateHeroAvatar(Integer userId, String heroAvatar) {

        if(userId == null || heroAvatar == null){
            return;
        }

        try(SqlSession sqlSession = SqlSessionHolder.getInstance().openSession()){

            sqlSession.getMapper(UserMapper.class)
                    .updateHeroAvatar(userId,heroAvatar);

        }catch (Exception ex){
            LOGGER.error("",ex);
        }

    }
}
