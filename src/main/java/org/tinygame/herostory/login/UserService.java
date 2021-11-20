package org.tinygame.herostory.login;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.SqlSessionHolder;
import org.tinygame.herostory.login.mapper.UserMapper;
import org.tinygame.herostory.model.UserEntity;

import java.util.Objects;

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


    public UserEntity login(String username,String password){

        if(null == username || null == password){
            return null;
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

            if(!Objects.equals(userEntity.getPassword(),password)){
                throw new RuntimeException("用户名或密码错误");
            }
        }catch (Exception e){
            LOGGER.error("",e);
        }

        return userEntity;
    }

}
