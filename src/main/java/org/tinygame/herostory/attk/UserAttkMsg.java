package org.tinygame.herostory.attk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户攻击消息
 * @author hugangquan
 * @date 2021/11/27 21:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAttkMsg {

    /**
     * 攻击用户
     */
    private Integer attkUserId;

    /**
     * 目标用户
     */
    private Integer targetId;

}
