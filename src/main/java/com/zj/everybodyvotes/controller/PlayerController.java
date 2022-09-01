package com.zj.everybodyvotes.controller;

import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zj.everybodyvotes.annotaation.Authority;
import com.zj.everybodyvotes.base.BasePageRequest;
import com.zj.everybodyvotes.constant.CommonResponseEnum;
import com.zj.everybodyvotes.constant.ImgTypeConstant;
import com.zj.everybodyvotes.constant.RoleEnum;
import com.zj.everybodyvotes.domain.SysUserActivity;
import com.zj.everybodyvotes.domain.VotesActivityVotes;
import com.zj.everybodyvotes.domain.request.ActivityPlayerPageRequest;
import com.zj.everybodyvotes.domain.request.CePlayerRequest;
import com.zj.everybodyvotes.domain.request.EnterActivityRequest;
import com.zj.everybodyvotes.domain.response.*;
import com.zj.everybodyvotes.exception.ActivityException;
import com.zj.everybodyvotes.exception.UserException;
import com.zj.everybodyvotes.service.IActivityService;
import com.zj.everybodyvotes.service.ISysUserActivityService;
import com.zj.everybodyvotes.service.ISysUserService;
import com.zj.everybodyvotes.service.IVotesActivityVotesService;
import com.zj.everybodyvotes.utils.DateUtil;
import com.zj.everybodyvotes.utils.FileTypeUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author cuberxp
 * @date 2021/5/15 3:04 下午
 */
@RestController
@AllArgsConstructor
@RequestMapping("/player")
public class PlayerController {

    private final ISysUserActivityService iSysUserActivityService;

    private final ISysUserService iSysUserService;

    private final IVotesActivityVotesService iVotesActivityVotesService;

    private final IActivityService iActivityService;

    /**
     * 管理员添加选手到中活动,同时创建这个用户
     */
    @Authority(role = { RoleEnum.ADMIN, RoleEnum.USER })
    @PostMapping("/activity/{activityId}")
    public BaseResponse addPlayer(@PathVariable("activityId") Long activityId,
                                  @Validated @RequestBody CePlayerRequest cePlayerRequest) {
        iSysUserActivityService.adminCreatePlayer(activityId, cePlayerRequest);
        return new BaseResponse();
    }

    /**
     * 上传选手封面图
     */
    @Authority(role = { RoleEnum.ADMIN, RoleEnum.USER })
    @PostMapping("/image")
    public CommonsResponse<String> uploadImage(@RequestPart("file") MultipartFile mulFile) throws IOException, UfileServerException, UfileClientException {
        ImgTypeConstant type = FileTypeUtil.getType(mulFile);

        if (type == null || ImgTypeConstant.XLS == type || ImgTypeConstant.XLSX == type) {
            throw new UserException(CommonResponseEnum.FILE_ERROR);
        }
        String imgUrl = iSysUserService.uploadCloudFile(mulFile.getInputStream(), mulFile.getContentType(), type.getSuffix());

        return new CommonsResponse<>(imgUrl);
    }

    /**
     * 获取获手编号
     */
    @GetMapping("/activity/{activityId}/numbers")
    public CommonsResponse<Integer> selectActivityNumber(@PathVariable("activityId") Long activityId) {
        LambdaQueryWrapper<SysUserActivity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUserActivity::getActivityId, activityId);

        int count = iSysUserActivityService.count(queryWrapper) + 1;
        return new CommonsResponse<>(count);
    }

    /**
     * 获取选手信息
     */
    @Authority(role = {RoleEnum.USER, RoleEnum.ADMIN})
    @GetMapping("/{playerId}")
    public CommonsResponse<SysUserActivity> selectActivityInfo(@PathVariable("playerId") Long playerId) {
        SysUserActivity byId = iSysUserActivityService.getById(playerId);

        return new CommonsResponse<>(byId);
    }

    /**
     * 获取己参加活动的选手分页信息
     */
    @GetMapping("/page")
    public CommonsResponse<IPage<ActivityPlayerResponse>> activityPage(@Validated @ModelAttribute ActivityPlayerPageRequest activityPlayerPageRequest) {
        System.out.println(activityPlayerPageRequest);
        IPage<ActivityPlayerResponse> activityPlayerResponseIPage = iSysUserActivityService.selectSysUserPlayerPage(activityPlayerPageRequest);

        return new CommonsResponse<>(activityPlayerResponseIPage);
    }

    /**
     * 删除该选手手的报名数据
     * id 选手id
     *
     */
    @Authority(role = { RoleEnum.ADMIN, RoleEnum.USER })
    @DeleteMapping("/{id}/activity/{activityId}")
    public BaseResponse deleteActivity(@PathVariable("id") Long id,
                                       @PathVariable("activityId") Long activityId) {
        iSysUserActivityService.removeById(id);

        LambdaQueryWrapper<VotesActivityVotes> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        objectLambdaQueryWrapper.eq(VotesActivityVotes::getUserActivityId, id)
                                .eq(VotesActivityVotes::getActivityId, activityId);
        iVotesActivityVotesService.remove(objectLambdaQueryWrapper);


        return new BaseResponse();
    }

    /**
     * 修改选手活动的信息
     */
    @Authority(role = { RoleEnum.ADMIN, RoleEnum.USER })
    @PutMapping("/{id}")
    public BaseResponse updateActivityPlayer(@PathVariable("id") Long id, @Validated @RequestBody CePlayerRequest cePlayerRequest) {
        SysUserActivity sysUserActivity = new SysUserActivity();
        BeanUtils.copyProperties(cePlayerRequest, sysUserActivity);
        sysUserActivity.setId(id);

        iSysUserActivityService.updateById(sysUserActivity);

        return new BaseResponse();
    }

    /**
     * 修改活动选手的 审核
     */
    //@Authority(role = { RoleEnum.ADMIN, RoleEnum.USER })
    @PutMapping("/{id}/review/status")
    public BaseResponse updateReviewStatus(@PathVariable("id") Long id,
                                           @RequestParam("status") Boolean status) {

        SysUserActivity sysUserActivity = new SysUserActivity();
        sysUserActivity.setId(id);
        sysUserActivity.setReview(status);
        iSysUserActivityService.updateById(sysUserActivity);

        return new BaseResponse();
    }

    /**
     * 增加活动的选手的浏览量
     */
    @PostMapping("/{id}")
    public BaseResponse addPlayerViews(@PathVariable("id") Long id) {
        UpdateWrapper<SysUserActivity> update = Wrappers.update();
        update.set("views", "views = views + 1")
            .eq("id", id);
        iSysUserActivityService.update(update);

        return new BaseResponse();
    }

    /**
     * 查看活动选手的投票日志
     */
    @Authority(role = { RoleEnum.ADMIN, RoleEnum.USER })
    @GetMapping("/{id}")
    public CommonsResponse<IPage<ActivityPlayerLogResponse>> getPlayerVoteLog(@PathVariable("id") Long id, @Validated @ModelAttribute BasePageRequest basePageRequest) {
        IPage<ActivityPlayerLogResponse> iPage = iVotesActivityVotesService.selectVoteLog(id, basePageRequest);
        return new CommonsResponse<>(iPage);
    }

    /**
     * 导出选手数据
     */
    @GetMapping("/activity/{id}")
    public void downloadFile(@PathVariable("id") Long activityId, HttpServletResponse response) throws IOException {
        System.out.println("导出选手数据");
        iSysUserActivityService.exportPlayer(response, activityId);
    }

    /**
     * 选手报名参加活动
     */
    @Authority(role = {RoleEnum.USER, RoleEnum.ADMIN})
    @PostMapping("/enter/activity")
    public BaseResponse enter(@Validated @RequestBody EnterActivityRequest enterActivityRequest) {
        ActivityInfoAllResponse activityInfoAllResponse = iActivityService.selectInfoAll(enterActivityRequest.getActivityId());

        // 选手报名未开启
        if (!activityInfoAllResponse.getSignFlag()) {
            return new BaseResponse(403, "不好意思！该活动的选报名还未开启");
        }

        long playerNumber = Optional.ofNullable(iSysUserActivityService.getMaxNumber(enterActivityRequest.getActivityId()))
                .orElse(0L) + 1;

        LambdaQueryWrapper<SysUserActivity> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        objectLambdaQueryWrapper
                .eq(SysUserActivity::getActivityId, enterActivityRequest.getActivityId())
                .eq(SysUserActivity::getUserId, enterActivityRequest.getUserId());
        int countCurrentPlayer = iSysUserActivityService.count(objectLambdaQueryWrapper);

        if (countCurrentPlayer > 0) {
            throw new ActivityException(403, "你己经报过名了哦!!!");
        }

        boolean reviewFlag = true;
        // 选手报名审核开启
        if (activityInfoAllResponse.getSingUpFlag()) {
            reviewFlag = false;
        }

        SysUserActivity sysUserActivity = new SysUserActivity()
                                .setRemark(enterActivityRequest.getRemark())
                                .setViews(0L)
                                .setReview(reviewFlag)
                                .setUserId(enterActivityRequest.getUserId())
                                .setActivityId(enterActivityRequest.getActivityId())
                                .setNumber(playerNumber)
                                .setGroupId(enterActivityRequest.getGroupId())
                                .setImgUrl(enterActivityRequest.getImgUrl())
                                .setCreateTime(DateUtil.localDateTimeToStampTime(LocalDateTime.now()) * 1000)
                ;
        System.out.println(sysUserActivity);
        iSysUserActivityService.save(sysUserActivity);

        return new BaseResponse();
    }

    /**
     * 审核通过
     */
    @Authority(role = { RoleEnum.ADMIN, RoleEnum.USER })
    @PutMapping("/review")
    public BaseResponse updateReviewStatus(@RequestParam("activityUserId") long activityUserId) {
        SysUserActivity sysUserActivity = new SysUserActivity()
                                .setId(activityUserId)
                                .setReview(true);

        iSysUserActivityService.updateById(sysUserActivity);
        return new BaseResponse();
    }
}
