package com.zj.everybodyvotes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.everybodyvotes.base.BasePageRequest;
import com.zj.everybodyvotes.domain.VotesActivityPicture;
import com.zj.everybodyvotes.domain.VotesActivityVotes;
import com.zj.everybodyvotes.domain.request.ActivityVoteRequest;
import com.zj.everybodyvotes.domain.response.ActivityPlayerLogResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cuberxp
 * @date 2021/5/14 10:37 下午
 */
public interface IVotesActivityVotesService extends IService<VotesActivityVotes> {
    IPage<ActivityPlayerLogResponse> selectVoteLog(Long id, BasePageRequest basePageRequest);

    void addActivityVote(ActivityVoteRequest activityVoteRequest, HttpServletRequest request);
}
