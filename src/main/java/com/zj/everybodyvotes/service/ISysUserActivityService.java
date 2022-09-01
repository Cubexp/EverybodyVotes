package com.zj.everybodyvotes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.everybodyvotes.domain.SysUserActivity;
import com.zj.everybodyvotes.domain.request.ActivityPlayerPageRequest;
import com.zj.everybodyvotes.domain.request.CePlayerRequest;
import com.zj.everybodyvotes.domain.response.ActivityPlayerResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author cuberxp
 * @date 2021/5/14 10:38 下午
 */
public interface ISysUserActivityService extends IService<SysUserActivity> {
    void adminCreatePlayer(Long activityId, CePlayerRequest cePlayerRequest);

    IPage<ActivityPlayerResponse> selectSysUserPlayerPage(ActivityPlayerPageRequest activityPlayerPageRequest);

    void exportPlayer(HttpServletResponse response, Long activityId) throws IOException;

    Long getMaxNumber(Long activityId);

}
