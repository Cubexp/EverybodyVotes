package com.zj.everybodyvotes.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author cuberxp
 * @date 2021/5/15 10:04 上午
 */
@Data
@Accessors(chain = true)
@TableName("sys_news_type")
public class SysNewsType {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String remark;
}
