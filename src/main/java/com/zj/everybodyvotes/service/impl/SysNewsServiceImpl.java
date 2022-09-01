package com.zj.everybodyvotes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zj.everybodyvotes.dao.INewsDao;
import com.zj.everybodyvotes.domain.SysNews;
import com.zj.everybodyvotes.domain.request.NewsPageRequest;
import com.zj.everybodyvotes.domain.response.SysNewsResponse;
import com.zj.everybodyvotes.service.ISysNewsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author cuberxp
 * @date 2021/5/15 4:44 下午
 */
@Service
@AllArgsConstructor
public class SysNewsServiceImpl extends ServiceImpl<INewsDao, SysNews> implements ISysNewsService {

    private final INewsDao iNewsDao;

    @Override
    public IPage<SysNewsResponse> selectNewsPage(NewsPageRequest newsPageRequest) {
        IPage<SysNews> page = new Page<>(newsPageRequest.getCurrentPage(), newsPageRequest.getLimit());
        QueryWrapper<SysNews> query = Wrappers.query();
        query.eq(newsPageRequest.getTypeId() != 0, "snt.id", newsPageRequest.getTypeId())
                .eq(newsPageRequest.getStatus() != 2, "sn.status", newsPageRequest.getStatus())
                .like(newsPageRequest.getTitle() != null, "sn.title", newsPageRequest.getTitle())
        ;
        return iNewsDao.selectNewsPage(page, query);
    }
}
