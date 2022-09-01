package com.zj.everybodyvotes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zj.everybodyvotes.dao.IVotesActivityGroupingDao;
import com.zj.everybodyvotes.domain.VotesActivityGrouping;
import com.zj.everybodyvotes.service.IVotesActivityGroupingService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author cuberxp
 * @date 2021/5/14 10:39 下午
 */
@Service
public class VotesActivityGroupingServiceImpl extends ServiceImpl<IVotesActivityGroupingDao, VotesActivityGrouping> implements IVotesActivityGroupingService {
    @Override
    public void insertActivityPlayerGroups(String[] groups, Long activityId) {
        Collection<VotesActivityGrouping> collect = Arrays.stream(groups)
                .map(groupName -> new VotesActivityGrouping()
                        .setActivityId(activityId)
                        .setName(groupName)
                ).collect(Collectors.toList());

        this.saveBatch(collect, collect.size());
    }
}
