package com.zj.everybodyvotes.domain.response;

import lombok.Data;

/**
 * @author cuberxp
 * @date 2021/5/20 14:18
 * @since 1.0.0
 */
@Data
public class ActivitySortResponse {
    private Long number;

    private String name;

    /**
     * 票数
     */
    private Long voteCount;

    /**
     * 平均分
     */
    private Double averageScore;

}
