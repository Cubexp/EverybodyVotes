package com.zj.everybodyvotes.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zj.everybodyvotes.domain.VotesActivityVotes;
import com.zj.everybodyvotes.domain.response.ActivityPlayerLogResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author cuberxp
 * @date 2021/5/14 7:27 下午
 */
public interface IVotesActivityVotesDao extends BaseMapper<VotesActivityVotes> {
    @Select("SELECT vav.id, su.name, su.avatar, vav.ip, vav.create_time 'createTime', vav.value FROM votes_activity_votes vav \n" +
            "LEFT JOIN sys_user su ON su.id = vav.vote_person_id \n" +
            "${ew.customSqlSegment}"
    )
    IPage<ActivityPlayerLogResponse> voteLog(IPage<VotesActivityVotes> page, @Param(Constants.WRAPPER) Wrapper<VotesActivityVotes> wrapper);

    @Select("SELECT COUNT(*) FROM votes_activity_votes vav WHERE vav.activity_id = #{activityId} AND vav.user_activity_id = #{userActivityId}")
    Long votesPersonCount(@Param("activityId") Long activityId, @Param("userActivityId") Long userActivityId);
}
