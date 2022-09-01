package com.zj.everybodyvotes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zj.everybodyvotes.annotaation.Authority;
import com.zj.everybodyvotes.constant.RoleEnum;
import com.zj.everybodyvotes.domain.SysNews;
import com.zj.everybodyvotes.domain.SysNewsType;
import com.zj.everybodyvotes.domain.SysUser;
import com.zj.everybodyvotes.domain.request.CeNewsRequest;
import com.zj.everybodyvotes.domain.request.NewsPageRequest;
import com.zj.everybodyvotes.domain.response.BaseResponse;
import com.zj.everybodyvotes.domain.response.CommonsResponse;
import com.zj.everybodyvotes.domain.response.SysNewsResponse;
import com.zj.everybodyvotes.service.ISysNewsService;
import com.zj.everybodyvotes.service.ISysNewsTypeService;
import com.zj.everybodyvotes.service.ISysUserService;
import com.zj.everybodyvotes.utils.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author cuberxp
 * @date 2021/5/15 4:05 下午
 */
@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private final ISysNewsService iSysNewsService;

    private final ISysUserService iSysUserService;

    private final ISysNewsTypeService iSysNewsTypeService;

    @Authority(role = {RoleEnum.ADMIN})
    @PostMapping("/{newsId}")
    public BaseResponse createNews(@PathVariable("newsId") Long userId, @Validated @RequestBody CeNewsRequest ceNewsRequest) {
        SysNews sysNews = new SysNews();
        BeanUtils.copyProperties(ceNewsRequest, sysNews);
        sysNews.setCreateId(userId)
                .setCreateTime(DateUtil.localDateTimeToStampTime(LocalDateTime.now()) * 1000);
        iSysNewsService.save(sysNews);

        return new BaseResponse();
    }

    /**
     * 新闻分页查询
     */
    @GetMapping("/page")
    public CommonsResponse<IPage<SysNewsResponse>> newsPage(@Validated @ModelAttribute NewsPageRequest newsPageRequest) {
        IPage<SysNewsResponse> sysNewsResponseIPage = iSysNewsService.selectNewsPage(newsPageRequest);
        return new CommonsResponse<>(sysNewsResponseIPage);
    }

    /**
     * 查询新闻详细
     */
    @GetMapping("/{newsId}")
    public CommonsResponse<SysNewsResponse> getNewsInfo(@PathVariable("newsId") Long newsId) {
        SysNewsResponse sysNewsResponse = new SysNewsResponse();
        SysNews sysNews = iSysNewsService.getById(newsId);

        SysUser sysUser = iSysUserService.getById(sysNews.getCreateId());
        SysNewsType sysNewsType = iSysNewsTypeService.getById(sysNews.getTypeId());
        sysNewsResponse.setContent(sysNews.getContent());
        sysNewsResponse.setTitle(sysNews.getTitle());
        sysNewsResponse.setId(sysNews.getId());
        sysNewsResponse.setCreateTime(sysNews.getCreateTime());

        sysNewsResponse.setSysUser(sysUser);
        sysNewsResponse.setType(sysNewsType);

        return new CommonsResponse<>(sysNewsResponse);
    }

    @Authority(role = {RoleEnum.ADMIN})
    @PutMapping("/{newsId}")
    public BaseResponse updateNews(@PathVariable("newsId") Long newsId, @Validated @RequestBody CeNewsRequest ceNewsRequest) {
        SysNews sysNews = new SysNews();
        BeanUtils.copyProperties(ceNewsRequest, sysNews);
        sysNews.setId(newsId);

        iSysNewsService.updateById(sysNews);

        return new BaseResponse();
    }

    @Authority(role = {RoleEnum.ADMIN})
    @DeleteMapping("{newsId}")
    public BaseResponse deleteNews(@PathVariable("newsId") Long newsId) {
        iSysNewsService.removeById(newsId);

        return new BaseResponse();
    }

    @Authority(role = {RoleEnum.ADMIN})
    @DeleteMapping("/bath")
    public BaseResponse deleteNewsBath(@RequestParam("ids")ArrayList<Long> ids) {
        iSysNewsService.removeByIds(ids);

        return new BaseResponse();
    }
}
