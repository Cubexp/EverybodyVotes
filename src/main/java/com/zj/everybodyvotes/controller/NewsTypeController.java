package com.zj.everybodyvotes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zj.everybodyvotes.annotaation.Authority;
import com.zj.everybodyvotes.base.BasePageRequest;
import com.zj.everybodyvotes.constant.RoleEnum;
import com.zj.everybodyvotes.dao.INewsTypeDao;
import com.zj.everybodyvotes.domain.SysNewsType;
import com.zj.everybodyvotes.domain.request.NewsTypeRequest;
import com.zj.everybodyvotes.domain.response.BaseResponse;
import com.zj.everybodyvotes.domain.response.CommonsResponse;
import com.zj.everybodyvotes.service.ISysNewsTypeService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author cuberxp
 * @date 2021/5/15 9:41 下午
 */
@RestController
@AllArgsConstructor
@RequestMapping("/newsType")
public class NewsTypeController {

    private final ISysNewsTypeService iSysNewsTypeService;

    private final INewsTypeDao iNewsTypeDao;

    @Authority(role = {RoleEnum.ADMIN})
    @PostMapping("/")
    public BaseResponse createNews(@Validated @RequestBody NewsTypeRequest newsTypeRequest) {
        SysNewsType sysNewsType = new SysNewsType()
                .setName(newsTypeRequest.getName())
                .setRemark(newsTypeRequest.getRemark());

        iSysNewsTypeService.save(sysNewsType);
        return new BaseResponse();
    }

    @GetMapping("/page")
    public CommonsResponse<IPage<SysNewsType>> newsPage(@Validated @ModelAttribute BasePageRequest basePageRequest) {
        System.out.println(basePageRequest);
        IPage<SysNewsType> sysNewsTypeIPage = new Page<>(basePageRequest.getCurrentPage(), basePageRequest.getLimit());

        IPage<SysNewsType> sysNewsTypeIPage1 = iNewsTypeDao.selectPage(sysNewsTypeIPage, Wrappers.emptyWrapper());
        return new CommonsResponse<>(sysNewsTypeIPage1);
    }

    @GetMapping("/{id}")
    public CommonsResponse<SysNewsType> getNewsInfo(@PathVariable("id") Long typeId) {
        SysNewsType byId = iSysNewsTypeService.getById(typeId);
        return new CommonsResponse<>(byId);
    }

    @Authority(role = {RoleEnum.ADMIN})
    @PutMapping("/{id}")
    public BaseResponse updateNews(@PathVariable("id") Long id,
                                   @Validated @RequestBody NewsTypeRequest newsTypeRequest) {

        SysNewsType sysNewsType = new SysNewsType()
                .setId(id)
                .setName(newsTypeRequest.getName())
                .setRemark(newsTypeRequest.getRemark());

        System.out.println(sysNewsType);
        iSysNewsTypeService.updateById(sysNewsType);
        return new BaseResponse();
    }

    @Authority(role = {RoleEnum.ADMIN})
    @DeleteMapping("{id}")
    public BaseResponse deleteNews(@PathVariable("id") Long id) {
        iSysNewsTypeService.removeById(id);
        return new BaseResponse();
    }

    @Authority(role = {RoleEnum.ADMIN})
    @DeleteMapping("/bath")
    public BaseResponse deleteNewsBath(@RequestParam("ids") ArrayList<Long> ids) {
        iSysNewsTypeService.removeByIds(ids);
        return new BaseResponse();
    }

}
