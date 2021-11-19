package org.tinygame.herostory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户移动状态
 * @author hugangquan
 * @date 2021/11/19 11:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MoveState {

    //出发点x坐标
    private float fromPosX;

    //出发点y坐标
    private float fromPosY;

    //目的点x坐标
    private float toPosX;

    //目的点y坐标
    private float toPosY;

    //启程时间
    private long startTime;
    
}
