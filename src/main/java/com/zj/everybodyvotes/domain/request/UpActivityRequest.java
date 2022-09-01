package com.zj.everybodyvotes.domain.request;

import com.zj.everybodyvotes.domain.VotesActivityGrouping;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author cuberxp
 * @date 2021/5/21 23:47
 * @since 1.0.0
 */
@Data
public class UpActivityRequest {
    @Length(min = 3, max = 100, message = "活动标题长度{min} ~ {max}")
    private String title;

    @NotNull
    private Long beginTime;

    @NotNull
    private Long endTime;

    @NotBlank(message = "活动内容不能为空")
    private String ruleContent;

    /**
     * {@link com.zj.everybodyvotes.constant.activity.CoverTypeEnum}
     */
    @Range(min = 0, max = 1, message = "封面类型参数错误")
    private Integer coverType;

    private String videoCover;

    /**
     * 活动封面图集
     */
    private String[] imgsCover;

    @NotNull(message = "活动选手分组是否开启不能为空")
    private Boolean groupFlag;

    /**
     * 选手分组
     */
//    private List<VotesActivityGrouping> group;

    /**
     * 活动公告是否启动
     */
    @NotNull(message = "活动公告是否启动不能为空")
    private Boolean noticeFlag;

    private String notice;

    /**
     * 选手的排序方式
     * {@link com.zj.everybodyvotes.constant.activity.PlayerSortMethodEnum}
     */
    @Range(min = 1, max = 3)
    private Integer playerMethod;

    /**
     * 活动开启选手是否可以报名
     */
    @NotNull(message = "选手是否可以报名字段不能为空!")
    private Boolean signFlag;

    /**
     * 选手报名审核是否开启
     */
    @NotNull(message = "选手报名审核是否开启不能为空")
    private Boolean singUpFlag;

    /**
     * 投票规则
     * {@link com.zj.everybodyvotes.constant.activity.VoteTypeEnum}
     */
    @NotNull
    @Range(min = 1, max = 2)
    private Integer voteRuleType;

    /**
     * 选手投票数量
     */
    @Range(min = 1, max = 10000, message = "选手投票的数量范围为{min} ~ {max}")
    private Integer playerVoteCount;

    /**
     * 投票按钮类型 分数还是点击
     * {@link com.zj.everybodyvotes.constant.activity.VoteButtonTypeEnum}
     */
    @NotNull
    @Range(min = 1, max = 3)
    private Integer voteButtonType;

    /**
     * 如果是分数的话，他的开始范围和结束范围是多少？
     */
    private Double inputRangeBegin;

    private Double inputRangeEnd;

    @Range(min = 0, max = 1)
    private Integer secretVoteFlag;

    /**
     * 创建者id
     */
    @NotNull
    private Long createId;
}
