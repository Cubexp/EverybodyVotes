package com.zj.everybodyvotes.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 选手分组
 * @author cuberxp
 * @date 2021/5/10 3:54 下午
 */
@Data
@Accessors(chain = true)
@TableName("votes_activity_grouping")
public class VotesActivityGrouping {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动id
     */
    @TableField("activity_id")
    private Long activityId;

    /**
     * 分组名称
     */
    private String name;
}
