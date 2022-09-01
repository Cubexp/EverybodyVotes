package com.zj.everybodyvotes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zj.everybodyvotes.base.BasePageRequest;
import com.zj.everybodyvotes.constant.activity.ActivityConstant;
import com.zj.everybodyvotes.dao.IVotesActivityVotesDao;
import com.zj.everybodyvotes.domain.VotesActivityVotes;
import com.zj.everybodyvotes.domain.dto.VoteRedisDTO;
import com.zj.everybodyvotes.domain.request.ActivityVoteRequest;
import com.zj.everybodyvotes.domain.response.ActivityPlayerLogResponse;
import com.zj.everybodyvotes.service.IVotesActivityVotesService;
import com.zj.everybodyvotes.utils.DateUtil;
import com.zj.everybodyvotes.utils.IpUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 活动投票记录
 * @author cuberxp
 * @date 2021/5/14 10:42 下午
 */
@Service
@AllArgsConstructor
public class VotesActivityVotesServiceImpl extends ServiceImpl<IVotesActivityVotesDao, VotesActivityVotes> implements IVotesActivityVotesService {

    private final IVotesActivityVotesDao iVotesActivityVotesDao;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public IPage<ActivityPlayerLogResponse> selectVoteLog(Long id, BasePageRequest basePageRequest) {
        IPage<VotesActivityVotes> activityVotesIPage = new Page<>(basePageRequest.getCurrentPage(), basePageRequest.getLimit());

        QueryWrapper<VotesActivityVotes> query = Wrappers.query();
        query.eq("vav.user_activity_id", id);
        return iVotesActivityVotesDao.voteLog(activityVotesIPage, query);
    }

    @Override
    public void addActivityVote(ActivityVoteRequest activityVoteRequest, HttpServletRequest request) {
        VotesActivityVotes votesActivityVotes = new VotesActivityVotes()
                .setActivityId(activityVoteRequest.getActivityId())
                .setUserActivityId(activityVoteRequest.getPlayerId())
                .setCreateTime(DateUtil.localDateTimeToStampTime(LocalDateTime.now()))
                .setValue(activityVoteRequest.getValue())
                //投票者id
                .setVotePersonId(activityVoteRequest.getVotePersonId())
                .setIp(IpUtils.getIpAddr(request));
                ;
        iVotesActivityVotesDao.insert(votesActivityVotes);

        String redisKey;
        // 开启了匿名投票
        if (activityVoteRequest.getVotePersonId() == null) {
            redisKey = ActivityConstant.ACTIVITY_VOTE_PLAYER + ":" + activityVoteRequest.getActivityId() + ":" + IpUtils.getIpAddr(request);
        } else {
            redisKey = ActivityConstant.ACTIVITY_VOTE_PLAYER + ":" + activityVoteRequest.getActivityId() + ":" + activityVoteRequest.getVotePersonId();
        }

        VoteRedisDTO voteRedisDTO = new VoteRedisDTO();
        Integer count = Optional.ofNullable(redisTemplate.opsForValue().get(redisKey))
                .map(item -> ((VoteRedisDTO) item).getVoteCount())
                .orElse(0);

        long remainingTime = DateUtil.localDateToStampTime(LocalDate.now().plusDays(1)) - DateUtil.localDateTimeToStampTime(LocalDateTime.now());

        voteRedisDTO.setVoteCount(count + 1);
        voteRedisDTO.setBeginStartTime(LocalDateTime.now());
        redisTemplate.opsForValue().set(redisKey, voteRedisDTO, remainingTime, TimeUnit.SECONDS);
    }
}
