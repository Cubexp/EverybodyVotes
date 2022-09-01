package com.zj.everybodyvotes.domain.response;

import lombok.Data;

/**
 * @author cuberxp
 * @date 2021/5/14 11:26 下午
 */
@Data
public class ActivityInfoPartResponse {
    private String title;

    private Boolean status;

    private Long id;

    private Long createTime;

    private Long playerCount;

    private Long voteCount;

    private Long views;

    private Long beginTime;

    private Long endTime;

    private String activityImage;

    private Integer coverType;

    private String videoCover;

    private Integer passed;
}
