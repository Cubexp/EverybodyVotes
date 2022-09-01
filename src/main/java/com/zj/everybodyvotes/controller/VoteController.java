package com.zj.everybodyvotes.controller;

import com.zj.everybodyvotes.constant.activity.ActivityConstant;
import com.zj.everybodyvotes.constant.activity.VoteTypeEnum;
import com.zj.everybodyvotes.domain.dto.VoteRedisDTO;
import com.zj.everybodyvotes.domain.request.ActivityVoteRequest;
import com.zj.everybodyvotes.domain.response.ActivityInfoAllResponse;
import com.zj.everybodyvotes.domain.response.BaseResponse;
import com.zj.everybodyvotes.exception.ActivityException;
import com.zj.everybodyvotes.service.IActivityService;
import com.zj.everybodyvotes.service.IVotesActivityVotesService;
import com.zj.everybodyvotes.utils.DateUtil;
import com.zj.everybodyvotes.utils.IpUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 投票
 * @author cuberxp
 * @date 2021/5/16 10:10 上午
 */
@RestController
@AllArgsConstructor
@RequestMapping("/vote")
public class VoteController {
    private final HttpServletRequest request;

    private final IActivityService iActivityService;

    private final IVotesActivityVotesService iVotesActivityVotesService;

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 给选手投票
     */
    @PostMapping("/")
    public BaseResponse activityVote(@Validated @RequestBody ActivityVoteRequest activityVoteRequest) {
        this.validateVote(activityVoteRequest, request);

        iVotesActivityVotesService.addActivityVote(activityVoteRequest, request);
        return new BaseResponse();
    }

    private void validateVote(ActivityVoteRequest activityVoteRequest, HttpServletRequest request) {
        ActivityInfoAllResponse activityInfoAllResponse = iActivityService.selectInfoAll(activityVoteRequest.getActivityId());

        // 可能没有这个活动
        if (activityInfoAllResponse == null) {
            throw new ActivityException(403, "没有这个活动！");
        }

        long nowTime = DateUtil.localDateTimeToStampTime(LocalDateTime.now());
        if (!activityInfoAllResponse.getStatus()) {
            throw new ActivityException(403, "当前活动没有开启!");
        }

        // 判断是否为匿名投票
        if (!activityInfoAllResponse.getSecretVoteFlag() && activityVoteRequest.getVotePersonId() == null) {
            throw new ActivityException(403, "投票前请登陆，当前未开启匿名投票！〜");
        }

        int activityVoteTypeFlag = activityInfoAllResponse.getVoteRuleType();

        // 一个号每天能投n票，可将全部票投给同一个选手
        if (activityVoteTypeFlag == VoteTypeEnum.TYPE_ONE.getValue()) {
            //匿名投票
            if (activityVoteRequest.getVotePersonId() == null) {
                String secretKey = ActivityConstant.ACTIVITY_VOTE_PLAYER + ":" + activityInfoAllResponse.getId() + ":" + IpUtils.getIpAddr(request);

                int count = Optional.ofNullable(redisTemplate.opsForValue().get(secretKey))
                        .map(item -> ((VoteRedisDTO) item).getVoteCount())
                        .orElse(0);

                System.out.println(count + "匿名投票");
                if (count >= activityInfoAllResponse.getPlayerVoteCount()) {
                    throw new ActivityException(403, "每天只能投" + activityInfoAllResponse.getPlayerVoteCount() + "票哦");
                }
            } else {
                // 非匿名投票
                String secretKey = ActivityConstant.ACTIVITY_VOTE_PLAYER + ":" + activityInfoAllResponse.getId() + ":" + activityVoteRequest.getVotePersonId();

                //不改重复的了。。。我己经写的精神不太正常了。。。。
                int count = Optional.ofNullable(redisTemplate.opsForValue().get(secretKey))
                        .map(item ->((VoteRedisDTO) item).getVoteCount())
                        .orElse(0);

                System.out.println(count + "非匿名投票");
                if (count >= activityInfoAllResponse.getPlayerVoteCount()) {
                    throw new ActivityException(403, "每天只能投" + activityInfoAllResponse.getPlayerVoteCount() + "票哦");
                }
            }
            // 活动时间内只能投一票
        } else if (activityVoteTypeFlag == VoteTypeEnum.TYPE_THREE.getValue()) {
            // 开启的匿名投票。
            if (activityInfoAllResponse.getSecretVoteFlag()) {
                String secretKey = ActivityConstant.ACTIVITY_VOTE_PLAYER + ":" + activityInfoAllResponse.getId() + ":" + IpUtils.getIpAddr(request);

                if (redisTemplate.hasKey(secretKey)) {
                    // 代表这个ip匿名用户己经投过票了。
                    throw new ActivityException(403, "你己经投过票，根据该活动的规则，每个人用户活动期间只能投一次票哦！匿名投票一样的！");
                }
            } else {
                String secretKey = ActivityConstant.ACTIVITY_VOTE_PLAYER + ":" + activityInfoAllResponse.getId() + ":" + activityVoteRequest.getVotePersonId();

                if (redisTemplate.hasKey(secretKey)) {
                    throw new ActivityException(403, "你己经投过票了，根据该活动的规则，每个用户活动期间只能投一次票哦！");
                }
            }
        }
    }
}
