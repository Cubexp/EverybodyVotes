package com.zj.everybodyvotes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.everybodyvotes.domain.VotesActivityGrouping;

/**
 * @author cuberxp
 * @date 2021/5/14 10:38 下午
 */
public interface IVotesActivityGroupingService extends IService<VotesActivityGrouping> {
    void insertActivityPlayerGroups(String[] group, Long id);
}
