package com.zj.everybodyvotes.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zj.everybodyvotes.domain.SysUserActivity;
import com.zj.everybodyvotes.domain.response.ActivityPlayerExcelResponse;
import com.zj.everybodyvotes.domain.response.ActivityPlayerResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 选手与活动关联表
 * @author cuberxp
 * @date 2021/5/14 7:25 下午
 */
public interface ISysUserActivityDao extends BaseMapper<SysUserActivity> {

    @Select("SELECT sua.id, sua.activity_id 'activityId', su.name, su.phone, sua.user_id, sua.number, sua.views, sua.review,sua.remark, sua.create_time 'createTime', sua.img_url 'imgUrl',\n" +
            " vag.id 'group.id', vag.name 'group.name'\n" +
            "            FROM sys_user_activity sua\n" +
            "            INNER JOIN sys_user su ON su.id = sua.user_id \n" +
            "            left JOIN votes_activity_grouping vag ON vag.activity_id = sua.activity_id and sua.group_id = vag.id \n" +
            "${ew.customSqlSegment}"
    )
    @Results(id = "player", value = {
            @Result(column = "{activityId=activityId,userActivityId=sua.user_id}", property = "votesCount", one = @One(select = "com.zj.everybodyvotes.dao.IVotesActivityVotesDao.votesPersonCount"))
    })
    IPage<ActivityPlayerResponse> selectSysUserActivityPage(IPage<SysUserActivity> page, @Param(Constants.WRAPPER) Wrapper<SysUserActivity> wrapper);

    @Select("SELECT \n" +
            "\t su.name, \n" +
            "\t sua.number, sua.views, sua.review, sua.user_id, sua.activity_id\n" +
            "FROM sys_user_activity sua \n" +
            "INNER JOIN sys_user su ON sua.user_id = su.id \n" +
            "WHERE sua.activity_id = #{activityId}"
    )
    @Results(id = "player2", value = {
            @Result(column = "{activityId=sua.activity_id,userActivityId=sua.user_id}", property = "votesCount", one = @One(select = "com.zj.everybodyvotes.dao.IVotesActivityVotesDao.votesPersonCount"))
    })
    List<ActivityPlayerExcelResponse> exportList(Long activityId);

    @Select("SELECT MAX(number) FROM sys_user_activity WHERE activity_id = #{activityId}")
    Long selectNumber(Long activityId);
}
