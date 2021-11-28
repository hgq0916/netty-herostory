package org.tinygame.herostory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hugangquan
 * @date 2021/11/08 22:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    //用户初始血量
    public static final int INIT_HP = 100;

    //用户id
    private Integer id;

    private String username;

    //英雄头像
    private String heroAvatar;

    //当前血量
    private int currentHp;

    //移动状态
    MoveState moveState;

    /**
     * 死亡状态
     */
    private boolean died;

}
