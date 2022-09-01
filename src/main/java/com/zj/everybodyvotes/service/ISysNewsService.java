package com.zj.everybodyvotes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.everybodyvotes.domain.SysNews;
import com.zj.everybodyvotes.domain.request.NewsPageRequest;
import com.zj.everybodyvotes.domain.response.SysNewsResponse;

/**
 * @author cuberxp
 * @date 2021/5/15 4:43 下午
 */
public interface ISysNewsService extends IService<SysNews> {
    IPage<SysNewsResponse> selectNewsPage(NewsPageRequest newsPageRequest);
}
