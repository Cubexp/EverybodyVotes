package com.zj.everybodyvotes.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zj.everybodyvotes.domain.VotesActivity;
import com.zj.everybodyvotes.domain.response.ActivityInfoPartResponse;
import com.zj.everybodyvotes.domain.response.ActivitySortResponse;
import org.apache.ibatis.annotations.*;

/**
 * @author cuberxp
 * @date 2021/5/14 7:05 下午
 */
public interface IActivityDao extends BaseMapper<VotesActivity> {

    @Select("SELECT va.id, va.status, va.title, va.begin_time, va.end_time, va.create_time 'createTime', va.views, va.id,va.begin_time 'beginTime', va.end_time 'endTime', va.cover_type 'coverType', va.video_cover 'videoCover' , va.passed,\n" +
            "(SELECT COUNT(sua.id) \n" +
            "FROM sys_user_activity sua WHERE sua.activity_id = va.id )  as playerCount,\n" +
            "(SELECT COUNT(vav.id) FROM votes_activity_votes vav WHERE vav.activity_id = va.id) as voteCount\n" +
            "FROM votes_activity va \n" +
            "${ew.customSqlSegment}")
    IPage<ActivityInfoPartResponse> selectInfoPartPage(IPage<VotesActivity> page, @Param(Constants.WRAPPER) Wrapper<VotesActivity> wrapper);

    @Select("SELECT \n" +
            "\t su.name,sua.number,va.id,sua.user_id, sua.review,\n" +
            "\t ifnull((SELECT AVG(vav.value) FROM votes_activity_votes vav WHERE vav.activity_id = va.id AND vav.user_activity_id = su.id ), 0) 'averageScore'\n" +
            "FROM votes_activity va \n" +
            "INNER JOIN sys_user_activity sua ON sua.activity_id = va.id \n" +
            "INNER JOIN sys_user su ON su.id = sua.user_id \n" +
            "LEFT JOIN votes_activity_grouping vag ON vag.id = sua.group_id  \n" +
            "${ew.customSqlSegment}")
    @Results(id = "activity", value = {
            @Result(column = "{activityId=va.id,userActivityId=sua.user_id}", property = "voteCount", one = @One(select = "com.zj.everybodyvotes.dao.IVotesActivityVotesDao.votesPersonCount"))
    })
    IPage<ActivitySortResponse> selectActivitySortPage(IPage<VotesActivity> votesActivityIPage,  @Param(Constants.WRAPPER) QueryWrapper<VotesActivity> query);
}
