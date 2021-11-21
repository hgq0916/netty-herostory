package org.tinygame.herostory.login.mapper;

import org.apache.ibatis.annotations.Param;
import org.tinygame.herostory.model.UserEntity;

/**
 * @author hugangquan
 * @date 2021/11/20 21:55
 */
public interface UserMapper {

    UserEntity getUserByUsername(@Param("username") String username);


    int insertUser(UserEntity userEntity);

    /**
     * 更新英雄形象
     * @param userId
     * @param heroAvatar
     */
    int updateHeroAvatar(@Param("userId") Integer userId,@Param("heroAvatar") String heroAvatar);
}
