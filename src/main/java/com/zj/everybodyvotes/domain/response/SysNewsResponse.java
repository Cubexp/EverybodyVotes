package com.zj.everybodyvotes.domain.response;

import com.zj.everybodyvotes.domain.SysNewsType;
import com.zj.everybodyvotes.domain.SysUser;
import lombok.Data;

/**
 * @author cuberxp
 * @date 2021/5/15 7:54 下午
 */
@Data
public class SysNewsResponse {
    private Long id;

    private String content;

    private String title;

    private SysNewsType type;

    private SysUser sysUser;

    private Long createTime;

    private Integer status;
}
