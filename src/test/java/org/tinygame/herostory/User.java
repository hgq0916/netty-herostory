package org.tinygame.herostory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinygame.herostory.model.MoveState;

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

    //英雄头像
    private String heroAvatar;

    //当前血量
    /**
     * 使用volatile保证变量在线程间的可见性，但不能保证原子性
     */
    private volatile int currentHp;

    //移动状态
    MoveState moveState;

    public synchronized void substractHp(int hp){
        this.currentHp = this.currentHp - hp;
    }

}
