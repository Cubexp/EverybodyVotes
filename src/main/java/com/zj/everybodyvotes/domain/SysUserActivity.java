package com.zj.everybodyvotes.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 选手与活动关联表
 * @author cuberxp
 * @date 2021/5/10 3:46 下午
 */
@Data
@Accessors(chain = true)
@TableName("sys_user_activity")
public class SysUserActivity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("activity_id")
    private Long activityId;

    @TableField("user_id")
    private Long userId;

    /**
     * 选手编号
     */
    private Long number;

    private Long views;

    /**
     * 审核是否通过
     */
    private Boolean review;

    /**
     * 描述
     */
    private String remark;

    /**
     * 分组id
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 参加时间
     */
    @TableField("create_time")
    private Long createTime;

    /**
     * 在活动中的封面图片
     */
    @TableField("img_url")
    private String imgUrl;
}
