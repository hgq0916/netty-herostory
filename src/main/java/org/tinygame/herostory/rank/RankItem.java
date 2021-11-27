package org.tinygame.herostory.rank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排名信息
 * @author hugangquan
 * @date 2021/11/27 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankItem {

    /**
     * 排名id
     */
    private Integer rankId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 英雄头像
     */
    private String heroAvator;

    /**
     * 胜利次数
     */
    private int win;

}
