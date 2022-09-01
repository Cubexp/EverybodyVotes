package com.zj.everybodyvotes.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author cuberxp
 * @date 2021/5/10 2:59 下午
 */
@Data
@Accessors(chain = true)
@TableName("votes_activity")
public class VotesActivity {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动是否开启
     */
    private Boolean status;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Long createTime;

    /**
     * 浏览量
     */
    private Long views;

    /**
     * 活动开始时音
     */
    @TableField(value = "begin_time")
    private Long beginTime;

    /**
     * 活动结束时间
     */
    @TableField(value = "end_time")
    private Long endTime;

    /**
     * 活动内容
     */
    @TableField("rule_content")
    private String ruleContent;

    /**
     * 封面类型 图片或者视频
     */
    @TableField("cover_type")
    private Integer coverType;

    /**
     * 视频封面
     */
    @TableField("video_cover")
    private String videoCover;

    /**
     * 活动公告是否启动
     */
    @TableField("notice_flag")
    private Boolean noticeFlag;

    /**
     * 公告内容
     */
    private String notice;

    /**
     * 活动开启选手是否可以报名
     */
    @TableField("sign_flag")
    private Boolean signFlag;

    /**
     * 选手报名审核是否开启
     */
    @TableField("sing_up_flag")
    private Boolean singUpFlag;

    /**
     * 选手排序方式
     * 按编号,按分数？
     */
    @TableField("player_method")
    private Integer playerMethod;

    /**
     * 投票规则
     * {@link com.zj.everybodyvotes.constant.activity.VoteTypeEnum}
     */
    @TableField("vote_rule_type")
    private Integer voteRuleType;

    /**
     * 选手投票数量
     */
    @TableField("player_vote_count")
    private Integer playerVoteCount;

    /**
     * 投票按钮类型 分数还是点击
     */
    @TableField("vote_button_type")
    private Integer voteButtonType;

    /**
     * 如果是分数的话，他的开始范围和结束范围是多少？
     */
    @TableField("input_range_begin")
    private Double inputRangeBegin;

    @TableField("input_range_end")
    private Double inputRangeEnd;

    /**
     * 是否开启分组
     */
    @TableField("group_flag")
    private Boolean groupFlag;

    /**
     * 是否开启匿名投票
     */
    @TableField("secret_vote_flag")
    private Boolean secretVoteFlag;

    @TableField("create_id")
    private Long createId;

    /**
     * 活动是否审核是否通过
     * 默认是1审核通过
     */
    private Integer passed;
}
