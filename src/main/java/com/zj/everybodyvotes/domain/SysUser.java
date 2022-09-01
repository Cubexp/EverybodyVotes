package com.zj.everybodyvotes.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author cuberxp
 * @date 2021/5/7 9:12 下午
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
public class SysUser {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String password;

    @TableField(value = "role_name")
    private String roleName;

    private Integer gender;

    private String avatar;

    private String source;

    private String uuid;

    @TableField(value = "register_time")
    private Long registerTime;
}
