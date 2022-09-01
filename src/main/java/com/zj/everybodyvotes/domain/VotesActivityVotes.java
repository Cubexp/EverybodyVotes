package com.zj.everybodyvotes.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 投票记录
 * @author cuberxp
 * @date 2021/5/10 3:28 下午
 */
@Data
@Accessors(chain = true)
@TableName("votes_activity_votes")
public class VotesActivityVotes {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 投票者用户id,如果活动开启了匿名投票，可以为空
     */
    @TableField("vote_person_id")
    private Long votePersonId;

    private String ip;

    @TableField("activity_id")
    private Long activityId;

    @TableField("create_time")
    private Long createTime;

    /**
     * 值
     */
    private Double value;

    /**
     * 投票对象id
     */
    @TableField("user_activity_id")
    private Long userActivityId;
}
