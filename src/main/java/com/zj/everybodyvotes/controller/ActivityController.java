package com.zj.everybodyvotes.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zj.everybodyvotes.annotaation.Authority;
import com.zj.everybodyvotes.base.BasePageRequest;
import com.zj.everybodyvotes.constant.RoleEnum;
import com.zj.everybodyvotes.constant.activity.VoteButtonTypeEnum;
import com.zj.everybodyvotes.domain.*;
import com.zj.everybodyvotes.domain.request.CeActivityGroupRequest;
import com.zj.everybodyvotes.domain.request.CeActivityRequest;
import com.zj.everybodyvotes.domain.request.UpActivityRequest;
import com.zj.everybodyvotes.domain.response.*;
import com.zj.everybodyvotes.exception.ActivityException;
import com.zj.everybodyvotes.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 活动相关接口
 * @author cuberxp
 * @date 2021/5/10 2:46 下午
 */
@RestController
@RequestMapping("/activity")
@AllArgsConstructor
public class ActivityController {

    private final IActivityService iActivityService;

    private final IVotesActivityGroupingService iVotesActivityGroupingService;

    @Autowired
    private IVotesActivityPictureService iVotesActivityPictureService;

    @Autowired
    private ISysUserActivityService iSysUserActivityService;

    @Autowired
    private IVotesActivityVotesService iVotesActivityVotesService;


    @GetMapping("/testGet")
    public BaseResponse createActivity1() {
        return new BaseResponse();
    }

    /**
     * 创建活动
     */
    @Authority(role = {RoleEnum.ADMIN, RoleEnum.USER})
    @PostMapping("/create")
    public BaseResponse createActivity(@Validated @RequestBody CeActivityRequest ceActivityRequest) {
        System.out.println(ceActivityRequest);
        this.ceActivityValidator(ceActivityRequest);
        iActivityService.createActivity(ceActivityRequest);

        return new BaseResponse();
    }

    /**
     * 修改活动状态 开启关闭
     */
    @Authority(role = {RoleEnum.ADMIN, RoleEnum.USER})
    @PutMapping("/{id}/status")
    public BaseResponse updateActivityStatus(@PathVariable("id") Long activityId,
                                             @RequestParam("status") Boolean status) {
        iActivityService.updateStatus(activityId, status);

        return new BaseResponse();
    }

    /**
     * 修改活动审核状态 开启关闭
     */
    @Authority(role = {RoleEnum.ADMIN})
    @PutMapping("/{id}/passed")
    public BaseResponse updateActivityPassed(@PathVariable("id") Long activityId,
                                             @RequestParam("passed") Integer passed) {
        iActivityService.updateById(new VotesActivity()
                .setId(activityId)
                .setStatus(passed == 1)
                .setPassed(passed)
        );

        return new BaseResponse();
    }

    /**
     * 获取活动的分页数据
     */
    @Authority(role = {RoleEnum.ADMIN, RoleEnum.USER})
    @GetMapping("/part/{userId}")
    public CommonsResponse<IPage<ActivityInfoPartResponse>> getActivityInfoPart(@PathVariable("userId") Long userId,
                                                                                @Validated @ModelAttribute BasePageRequest basePageRequest) {
        IPage<ActivityInfoPartResponse> activityInfoPartResponse = iActivityService.getInfoPartPage(basePageRequest, userId);
        return new CommonsResponse<>(activityInfoPartResponse);
    }

    /**
     * 获取活动的所有数据
     */
    @Authority(role = {RoleEnum.ADMIN})
    @GetMapping("/all/info")
    public CommonsResponse<IPage<ActivityInfoPartResponse>> getActivityInfoAllPage(Integer currentPage,
                                                                                   Integer limit) {
        BasePageRequest basePageRequest = new BasePageRequest();
        basePageRequest.setCurrentPage(currentPage);
        basePageRequest.setLimit(limit);

        IPage<ActivityInfoPartResponse> activityInfoPartResponse = iActivityService.getInfoPageAll(basePageRequest);
        return new CommonsResponse<>(activityInfoPartResponse);
    }

    /**
     * 查看活动链接及二维码
     */
    @Authority(role = {RoleEnum.ADMIN, RoleEnum.USER})
    @GetMapping("/{id}/code")
    public CommonsResponse<ActivityQRCodeResponse> getActivityQRCode(@PathVariable("id") Long id) throws IOException {
        ActivityQRCodeResponse activityQRCodeResponse = iActivityService.createActivityQRCode(id);
        return new CommonsResponse<>(activityQRCodeResponse);
    }

    /**
     * 查看活动的全部数据
     */
    @GetMapping("/{id}/all")
    public CommonsResponse<ActivityInfoAllResponse> getActivityAllInfo(@PathVariable("id") Long id) {
        ActivityInfoAllResponse activityInfoAllResponse = iActivityService.selectInfoAll(id);
        return new CommonsResponse<>(activityInfoAllResponse);
    }

    /**
     * 修改活动
     */
    @Authority(role = {RoleEnum.ADMIN, RoleEnum.USER})
    @PutMapping("/{id}")
    public BaseResponse updateActivity(@PathVariable("id") Long id, @Validated @RequestBody UpActivityRequest ceActivityRequest) {
        iActivityService.updateActivity(id, ceActivityRequest);
        return new BaseResponse();
    }

    /**
     * 增加活动的浏览数
     */
    @PostMapping("/{id}")
    public BaseResponse addActivityViews(@PathVariable("id") Long id) {
        iActivityService.addViews(id);
        return new BaseResponse();
    }

    @GetMapping("/{id}/sort")
    public CommonsResponse<IPage<ActivitySortResponse>> getActivitySort(@PathVariable("id") Long activityId,
                                                                 @RequestParam("groupId") Long groupId) {
        IPage<ActivitySortResponse> activitySortResponseIPage = iActivityService.getActivitySort(activityId, groupId);

        activitySortResponseIPage.getRecords().forEach(item -> {
            if (item.getAverageScore() == null) {
                item.setAverageScore(0.0);
            }
        });
        return new CommonsResponse<>(activitySortResponseIPage);
    }

    @DeleteMapping("/group/{groupId}")
    public BaseResponse deleteGroup(@PathVariable("groupId") Long groupId) {
        iVotesActivityGroupingService.removeById(groupId);

        return new BaseResponse();
    }

    @PutMapping("/group/{groupId}")
    public BaseResponse postGroup(@PathVariable("groupId") Long groupId, @RequestParam("name") String name) {
        VotesActivityGrouping votesActivityGrouping = new VotesActivityGrouping();
        votesActivityGrouping.setId(groupId).setName(name);
        iVotesActivityGroupingService.updateById(votesActivityGrouping);

        return new BaseResponse();
    }

    @PostMapping("/group")
    public CommonsResponse<Long> deleteGroup(@RequestBody CeActivityGroupRequest ceActivityRequest) {
        VotesActivityGrouping votesActivityGrouping = new VotesActivityGrouping()
                .setActivityId(ceActivityRequest.getActivityId())
                .setName(ceActivityRequest.getName());
        iVotesActivityGroupingService.save(votesActivityGrouping);
        return new CommonsResponse<>(votesActivityGrouping.getId());
    }

    /**
     * 删除活动封面图片
     */
    @DeleteMapping("/cover/images")
    public BaseResponse deleteActivityCoverImages(@RequestParam("0")String imageUrl) {
        LambdaQueryWrapper<VotesActivityPicture> queryWrapper = Wrappers.lambdaQuery(VotesActivityPicture.class);
        queryWrapper.eq(VotesActivityPicture::getImgUrl, imageUrl);

        iVotesActivityPictureService.remove(queryWrapper);

        return new BaseResponse();
    }

    /**
     * 删除活动
     */
    @DeleteMapping("/{id}")
    public BaseResponse deleteActivity(@PathVariable("id") Long id) {
        iActivityService.removeById(id);

        LambdaQueryWrapper<VotesActivityPicture> votesActivityPictureLambdaQueryWrapper = Wrappers.lambdaQuery();
        votesActivityPictureLambdaQueryWrapper.eq(VotesActivityPicture::getActivityId, id);
        iVotesActivityPictureService.remove(votesActivityPictureLambdaQueryWrapper);

        LambdaQueryWrapper<SysUserActivity> sysUserActivityLambdaQueryWrapper = Wrappers.lambdaQuery();
        sysUserActivityLambdaQueryWrapper.eq(SysUserActivity::getActivityId, id);
        iSysUserActivityService.remove(sysUserActivityLambdaQueryWrapper);

        LambdaQueryWrapper<VotesActivityGrouping> votesActivityGroupingLambdaQueryWrapper = Wrappers.lambdaQuery();
        votesActivityGroupingLambdaQueryWrapper.eq(VotesActivityGrouping::getActivityId, id);
        iVotesActivityGroupingService.remove(votesActivityGroupingLambdaQueryWrapper);

        LambdaQueryWrapper<VotesActivityVotes> votesActivityVotesLambdaQueryWrapper = Wrappers.lambdaQuery();
        votesActivityVotesLambdaQueryWrapper.eq(VotesActivityVotes::getActivityId, id);
        iVotesActivityVotesService.remove(votesActivityVotesLambdaQueryWrapper);
        return new BaseResponse();
    }

    private void ceActivityValidator(CeActivityRequest ceActivityRequest) {
        if (ceActivityRequest.getBeginTime() >= ceActivityRequest.getEndTime()) {
            throw new ActivityException(403, "活动的开始时间不能大于等于结束时间");
        }

        // 如果是输入分数的活
        if (VoteButtonTypeEnum.INPUT_TYPE.getValue() == ceActivityRequest.getVoteButtonType()) {
            if (ceActivityRequest.getInputRangeEnd() < 0 || ceActivityRequest.getInputRangeBegin() < 0) {
                throw new ActivityException(403, "输入的分数规定范围不能是负数！！");
            }

            if (ceActivityRequest.getInputRangeBegin() >= ceActivityRequest.getInputRangeEnd()) {
                throw new ActivityException(403, "开始分数不能大于等于结束分数");
            }
        }
    }
}
