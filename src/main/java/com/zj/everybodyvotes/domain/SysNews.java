package com.zj.everybodyvotes.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author cuberxp
 * @date 2021/5/15 10:02 上午
 */
@Data
@Accessors(chain = true)
@TableName("sys_news")
public class SysNews {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String content;

    private String title;

    @TableField("type_id")
    private Long typeId;

    @TableField("create_id")
    private Long createId;

    @TableField("create_time")
    private Long createTime;

    /**
     * 新闻状态 己发布，草稿
     */
    private Integer status;
}
