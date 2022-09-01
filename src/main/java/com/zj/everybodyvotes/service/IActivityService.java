package com.zj.everybodyvotes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.everybodyvotes.base.BasePageRequest;
import com.zj.everybodyvotes.domain.SysNewsType;
import com.zj.everybodyvotes.domain.VotesActivity;
import com.zj.everybodyvotes.domain.request.CeActivityRequest;
import com.zj.everybodyvotes.domain.request.UpActivityRequest;
import com.zj.everybodyvotes.domain.response.*;

import java.io.IOException;

/**
 * @author cuberxp
 * @date 2021/5/14 7:30 下午
 */
public interface IActivityService extends IService<VotesActivity> {

    /**
     * 创建活动
     */
    void createActivity(CeActivityRequest ceActivityRequest);

    void updateStatus(Long activityId, Boolean status);

    IPage<ActivityInfoPartResponse> getInfoPartPage(BasePageRequest basePageRequest,  Long userId);

    ActivityQRCodeResponse createActivityQRCode(Long id) throws IOException;

    ActivityInfoAllResponse selectInfoAll(Long id);

    void updateActivity(Long id, UpActivityRequest ceActivityRequest);

    void addViews(Long id);

    IPage<ActivitySortResponse> getActivitySort(Long activityId, Long groupId);

    /**
     * 获取活动的所有数据
     */
    IPage<ActivityInfoPartResponse> getInfoPageAll(BasePageRequest basePageRequest);
}
