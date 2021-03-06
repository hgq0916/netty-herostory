package org.tinygame.herostory.login;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.AsyncOperation;
import org.tinygame.herostory.AsyncThreadProcessor;
import org.tinygame.herostory.MainTheadProcessor;
import org.tinygame.herostory.SqlSessionHolder;
import org.tinygame.herostory.login.mapper.UserMapper;
import org.tinygame.herostory.model.UserEntity;
import org.tinygame.herostory.util.RedisUtil;
import redis.clients.jedis.Jedis;

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

        UserLoginAsyncOperation userLoginAsyncOperation = new UserLoginAsyncOperation(username,password,callback);

        int bindId = 0;

        if(StringUtils.isNotBlank(username)){
            bindId = username.charAt(username.length()-1);
        }

        AsyncThreadProcessor.getInstance().process(userLoginAsyncOperation,bindId);
    }


    private class UserLoginAsyncOperation implements AsyncOperation {

        private final String username;

        private final String password;

        private final Function<UserEntity,Void> callback;

        private UserEntity userEntity;

        public UserLoginAsyncOperation(String username,String password,Function<UserEntity,Void> callback){
            this.username = username;
            this.password = password;
            this.callback = callback;
        }

        @Override
        public void doAsync() {

            LOGGER.info("doAsync当前线程："+Thread.currentThread().getName());

            if(null == username || null == password){
                return;
            }

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

            }catch (Exception e){
                LOGGER.error("",e);
            }

            //将用户信息写入redis
            try(Jedis jedis = RedisUtil.getInstance().getRedis()){

                UserEntity simpleUserEntity = new UserEntity();
                simpleUserEntity.setId(userEntity.getId());
                simpleUserEntity.setUsername(userEntity.getUsername());
                simpleUserEntity.setHeroAvatar(userEntity.getHeroAvatar());

                jedis.hset("User:"+userEntity.getId(),"userInfo", JSON.toJSONString(simpleUserEntity));
                jedis.hset("User:"+userEntity.getId(),"win","0");
                jedis.hset("User:"+userEntity.getId(),"lose","0");
            }catch (Exception ex){
                LOGGER.error("",ex);
            }

        }

        @Override
        public void doFinish() {

            LOGGER.info("doFinish当前线程："+Thread.currentThread().getName());

            if(callback != null){
                callback.apply(userEntity);
            }
        }

    }




    /**
     * 更新英雄头像
     * @param userId
     * @param heroAvatar
     */
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

        //更新redis中的用户头像
        try(Jedis jedis = RedisUtil.getInstance().getRedis()){

            String userInfo = jedis.hget("User:" + userId, "userInfo");

            UserEntity userEntity = JSON.parseObject(userInfo, UserEntity.class);

            userEntity.setHeroAvatar(heroAvatar);

            jedis.hset("User:"+userId,"userInfo",JSON.toJSONString(userEntity));
        }catch (Exception ex){
            LOGGER.error("",ex);
        }

    }
}
