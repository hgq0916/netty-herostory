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

    //用户id
    private Integer id;

    //英雄头像
    private String heroAvatar;

    MoveState moveState;

}
