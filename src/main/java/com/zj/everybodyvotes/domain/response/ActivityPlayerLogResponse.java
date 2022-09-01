package com.zj.everybodyvotes.domain.response;

import lombok.Data;

/**
 * 活动选手的投票日志
 * @author cuberxp
 * @date 2021/5/15 11:40 下午
 */
@Data
public class ActivityPlayerLogResponse {
    private Integer id;

    private String name;

    private String avatar;

    private String ip;

    private Long createTime;

    private Double value;
}
