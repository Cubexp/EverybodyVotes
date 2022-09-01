package com.zj.everybodyvotes.domain.response;

import com.zj.everybodyvotes.domain.VotesActivityGrouping;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author cuberxp
 * @date 2021/5/15 12:19 上午
 */
@Data
@Accessors(chain = true)
public class ActivityInfoAllResponse {
    private Long id;

    private String title;

    /**
     * 活动是否开启
     */
    private Boolean status;

    private Long beginTime;

    private Long endTime;

    private String ruleContent;

    private Integer coverType;

    private String videoCover;

    private List<String> coverImages;

    private Boolean groupFlag;

    private List<VotesActivityGrouping> group;

    private Boolean noticeFlag;

    private String notice;

    private Integer playerMethod;

    private Boolean signFlag;

    /**
     * 活动开启选手是否可以报名
     */
    private Boolean singUpFlag;

    private Integer voteRuleType;

    private Integer playerVoteCount;

    private Integer voteButtonType;

    private Double inputRangeBegin;

    private Double inputRangeEnd;

    private Boolean secretVoteFlag;

    /**
     * 己报名选手数
     */
    private Integer playerCount;

    /**
     * 总投票
     */
    private Integer voteCount;

    private Long views;
}
