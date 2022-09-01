package com.zj.everybodyvotes.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 活动封面图片记录
 * @author cuberxp
 * @date 2021/5/10 3:58 下午
 */
@Data
@Accessors(chain = true)
@TableName("votes_activity_picture")
public class VotesActivityPicture {
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动id
     */
    @TableField("activity_id")
    private Long activityId;

    /**
     * 图片url
     */
    @TableField("img_url")
    private String imgUrl;

}
