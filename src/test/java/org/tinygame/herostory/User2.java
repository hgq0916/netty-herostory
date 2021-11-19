package org.tinygame.herostory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tinygame.herostory.model.MoveState;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hugangquan
 * @date 2021/11/08 22:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User2 {

    //用户初始血量
    public static final int INIT_HP = 100;

    //用户id
    private Integer id;

    //英雄头像
    private String heroAvatar;

    //当前血量
    private volatile int currentHp;

    //移动状态
    MoveState moveState;

    public synchronized void substractHp(int hp){
        this.currentHp = this.currentHp - hp;
    }

    /**
     * 一个用户攻击另一个用户
     * @param user
     */
    public  synchronized void attkUser(User2 user){
        user.substractHp(10);
    }

    /*public  void attkUser(User2 user){

        int hp = 0;
        synchronized(this){
            hp = 10;
        }

        user.substractHp(hp);
    }*/


}
