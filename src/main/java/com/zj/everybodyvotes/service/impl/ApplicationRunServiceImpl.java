package com.zj.everybodyvotes.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zj.everybodyvotes.constant.activity.ActivityConstant;
import com.zj.everybodyvotes.constant.activity.VoteTypeEnum;
import com.zj.everybodyvotes.dao.IActivityDao;
import com.zj.everybodyvotes.dao.ISysUserActivityDao;
import com.zj.everybodyvotes.domain.dto.VoteRedisDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author cuberxp
 * @date 2022/4/12 22:30
 * @since 1.0.0
 */
@Service
public class ApplicationRunServiceImpl implements InitializingBean {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IActivityDao iActivityDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 开启了匿名投票
        // 每次重启后，清理己投的票
        clearSpareVotes(iActivityDao, redisTemplate);
    }

    public static void clearSpareVotes(IActivityDao iActivityDao, RedisTemplate<String, Object> redisTemplate) {
        iActivityDao.selectList(Wrappers.emptyWrapper()).stream()
                .filter(activityItem -> activityItem.getStatus() && activityItem.getVoteRuleType() == VoteTypeEnum.TYPE_ONE.getValue())
                .forEach(votesActivity -> {
                    String redisKey = ActivityConstant.ACTIVITY_VOTE_PLAYER + ":" + votesActivity.getId() + ":*";

                    Set<String> keys = redisTemplate.keys(redisKey);

                    // 每天清理该活动的投票
                    for (String key : keys) {
                        VoteRedisDTO voteRedisDTO = (VoteRedisDTO)redisTemplate.opsForValue().get(key);

                        // 清除隔天的投票
                        if (voteRedisDTO.getBeginStartTime().getDayOfYear() != LocalDate.now().getDayOfYear()) {
                            redisTemplate.delete(keys);
                        }
                    }
                });
    }
}
