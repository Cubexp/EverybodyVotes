package com.zj.everybodyvotes.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zj.everybodyvotes.constant.activity.ActivityConstant;
import com.zj.everybodyvotes.constant.activity.VoteTypeEnum;
import com.zj.everybodyvotes.dao.IActivityDao;
import com.zj.everybodyvotes.dao.ISysUserActivityDao;
import com.zj.everybodyvotes.service.impl.ApplicationRunServiceImpl;
import com.zj.everybodyvotes.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author cuberxp
 * @date 2021/5/20 03:12
 * @since 1.0.0
 */
@Component
@EnableScheduling
public class ActivityPlayerVoteTask {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ISysUserActivityDao sysUserActivityDao;

    @Autowired
    private IActivityDao iActivityDao;

    @Scheduled(cron = "0 59 23 * * ?")
    public void cleanEveryDayVotes() {
        // 开启了匿名投票
        ApplicationRunServiceImpl.clearSpareVotes(iActivityDao, redisTemplate);
    }
}
