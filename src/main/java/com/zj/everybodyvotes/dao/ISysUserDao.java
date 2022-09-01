package com.zj.everybodyvotes.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zj.everybodyvotes.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author cuberxp
 * @date 2021/5/7 9:17 下午
 */
public interface ISysUserDao extends BaseMapper<SysUser> {

    @Update("UPDATE sys_user su SET su.password =#{password} WHERE su.phone= #{phone}")
    void updateUserPhone(@Param("phone") String phone, @Param("password") String password);
}
