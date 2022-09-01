package com.zj.everybodyvotes.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zj.everybodyvotes.domain.SysNews;
import com.zj.everybodyvotes.domain.response.SysNewsResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author cuberxp
 * @date 2021/5/15 10:06 上午
 */
public interface INewsDao extends BaseMapper<SysNews> {

    @Select("SELECT \n" +
            "\t sn.id, sn.status, sn.content, sn.create_time as 'createTime', sn.title, \n" +
            "\t snt.id 'type.id', snt.name 'type.name', snt.remark 'type.remark',\n" +
            "\t su.avatar 'sysUser.avatar', su.id 'sysUser.id', su.name 'sysUser.name' \n" +
            "FROM sys_news sn \n" +
            "INNER JOIN sys_news_type snt ON sn.type_id = snt.id\n" +
            "INNER JOIN sys_user su ON su.id = sn.create_id\n" +
            "${ew.customSqlSegment}")
    IPage<SysNewsResponse> selectNewsPage(IPage<SysNews> page, @Param(Constants.WRAPPER) Wrapper<SysNews> wrapper);
}
