package com.zj.everybodyvotes.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zj.everybodyvotes.base.BasePageRequest;
import com.zj.everybodyvotes.constant.CommonResponseEnum;
import com.zj.everybodyvotes.constant.activity.CoverTypeEnum;
import com.zj.everybodyvotes.constant.activity.VoteButtonTypeEnum;
import com.zj.everybodyvotes.dao.IActivityDao;
import com.zj.everybodyvotes.dao.ISysUserDao;
import com.zj.everybodyvotes.domain.*;
import com.zj.everybodyvotes.domain.request.CeActivityRequest;
import com.zj.everybodyvotes.domain.request.UpActivityRequest;
import com.zj.everybodyvotes.domain.response.ActivityInfoAllResponse;
import com.zj.everybodyvotes.domain.response.ActivityInfoPartResponse;
import com.zj.everybodyvotes.domain.response.ActivityQRCodeResponse;
import com.zj.everybodyvotes.domain.response.ActivitySortResponse;
import com.zj.everybodyvotes.service.*;
import com.zj.everybodyvotes.utils.DateUtil;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cuberxp
 * @date 2021/5/14 9:05 下午
 */
@Service
@AllArgsConstructor
public class ActivityServiceImpl extends ServiceImpl<IActivityDao, VotesActivity> implements IActivityService {

    private final IActivityDao iActivityDao;

    private final ISysUserActivityService iSysUserActivityService;

    private final IVotesActivityVotesService iVotesActivityVotesService;

    private final IVotesActivityPictureService iVotesActivityPictureService;

    private final IVotesActivityGroupingService iVotesActivityGroupingService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createActivity(CeActivityRequest ceActivityRequest) {
        VotesActivity votesActivity = new VotesActivity();

        BeanUtils.copyProperties(ceActivityRequest, votesActivity);
        votesActivity.setStatus(false);
        votesActivity.setCreateTime(DateUtil.localDateTimeToStampTime(LocalDateTime.now()) * 1000);
        votesActivity.setViews(0L);
        votesActivity.setSecretVoteFlag(ceActivityRequest.getSecretVoteFlag() == 1);

        System.out.println(votesActivity);
        // 插入活动
        iActivityDao.insert(votesActivity);

        // 活动图片封面设置
        if (ceActivityRequest.getCoverType() == 0) {
            iVotesActivityPictureService.insertActivityPictures(ceActivityRequest.getImgsCover(), votesActivity.getId());
        }

        // 选手分组是否开启
        if (ceActivityRequest.getGroupFlag()) {
            iVotesActivityGroupingService.insertActivityPlayerGroups(ceActivityRequest.getGroup(), votesActivity.getId());
        }
    }

    @Override
    public void updateStatus(Long activityId, Boolean status) {
        VotesActivity votesActivity = new VotesActivity()
                .setId(activityId)
                .setStatus(status);
        iActivityDao.updateById(votesActivity);
    }

    @Override
    public IPage<ActivityInfoPartResponse> getInfoPartPage(BasePageRequest basePageRequest, Long userId) {
        IPage<VotesActivity> votesActivityIPage = new Page<>(basePageRequest.getCurrentPage(), basePageRequest.getLimit());
        QueryWrapper<VotesActivity> query = Wrappers.query();
        query.eq("create_id", userId);

        IPage<ActivityInfoPartResponse> activityInfoPartResponseIPage = iActivityDao.selectInfoPartPage(votesActivityIPage,query );

        activityInfoPartResponseIPage.getRecords().forEach(item -> {
            if (item.getCoverType() == CoverTypeEnum.IMAGE_TYPE.getValue()) {
                // 获取封面图片
                LambdaQueryWrapper<VotesActivityPicture> pictureQueryWrapper = Wrappers.lambdaQuery();
                pictureQueryWrapper.eq(VotesActivityPicture::getActivityId, item.getId())
                        .select(VotesActivityPicture::getImgUrl);
                item.setActivityImage(iVotesActivityPictureService.list(pictureQueryWrapper).stream().findFirst().orElse(new VotesActivityPicture())
                        .getImgUrl());
            }
        });
        return activityInfoPartResponseIPage;
    }

    @Override
    public ActivityQRCodeResponse createActivityQRCode(Long id) throws IOException {
        String activityUlr = CommonResponseEnum.DEFAULT_WEBSITE_URL.getMessage() + id;
        String hostAddress = this.getClientIp();
//        activityUlr = activityUlr.replace("localhost", hostAddress);

        System.out.println(activityUlr);

        BufferedImage generate = QrCodeUtil.generate(activityUlr, 300, 300);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(generate, "png", stream);
        String qrBase64 = Base64.encode(stream.toByteArray());

        return new ActivityQRCodeResponse()
                .setActivityQRCode("data:image/png;base64," + qrBase64)
                .setActivityUrl(activityUlr);
    }

    private String getClientIp() throws SocketException {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces
                        .nextElement();

                // 去除回环接口，子接口，未运行和接口
                if (netInterface.isLoopback() || netInterface.isVirtual()
                        || !netInterface.isUp()) {
                    continue;
                }

                if (!netInterface.getDisplayName().contains("Intel")
                        && !netInterface.getDisplayName().contains("Realtek")) {
                    continue;
                }
                Enumeration<InetAddress> addresses = netInterface
                        .getInetAddresses();
                System.out.println(netInterface.getDisplayName());
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null) {
                        // ipv4
                        if (ip instanceof Inet4Address) {
                            System.out.println("ipv4 = " + ip.getHostAddress());
                            return ip.getHostAddress();
                        }
                    }
                }
                break;
            }
        return null;
    }

    @Override
    public ActivityInfoAllResponse selectInfoAll(Long id) {
        ActivityInfoAllResponse activityInfoAllResponse = new ActivityInfoAllResponse();

        VotesActivity votesActivity = iActivityDao.selectById(id);

        BeanUtils.copyProperties(votesActivity, activityInfoAllResponse);

        LambdaQueryWrapper<SysUserActivity> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
        objectLambdaQueryWrapper.eq(SysUserActivity::getActivityId, id)
                                .eq(SysUserActivity::getReview, 1);
        int playerCount = iSysUserActivityService.count(objectLambdaQueryWrapper);
        activityInfoAllResponse.setPlayerCount(playerCount);

        // 活动票数
        LambdaQueryWrapper<VotesActivityVotes> objectLambdaQueryWrapper1 = Wrappers.lambdaQuery();
        objectLambdaQueryWrapper1.eq(VotesActivityVotes::getActivityId, id);
        int votesCount = iVotesActivityVotesService.count(objectLambdaQueryWrapper1);
        activityInfoAllResponse.setVoteCount(votesCount);

        System.out.println(activityInfoAllResponse);
        if (activityInfoAllResponse.getCoverType() == CoverTypeEnum.IMAGE_TYPE.getValue()) {
            // 获取封面图片
            LambdaQueryWrapper<VotesActivityPicture> pictureQueryWrapper = Wrappers.lambdaQuery();
            pictureQueryWrapper.eq(VotesActivityPicture::getActivityId, id)
                    .select(VotesActivityPicture::getImgUrl);
            activityInfoAllResponse.setCoverImages(iVotesActivityPictureService.list(pictureQueryWrapper).stream()
                    .map(VotesActivityPicture::getImgUrl)
                    .collect(Collectors.toList()));
        }

        // 获取分组
        if (votesActivity.getGroupFlag()) {
            LambdaQueryWrapper<VotesActivityGrouping> groupQueryWrapper = Wrappers.lambdaQuery();
            groupQueryWrapper.eq(VotesActivityGrouping::getActivityId, id);
            activityInfoAllResponse.setGroup(iVotesActivityGroupingService.list(groupQueryWrapper));
        }

        return activityInfoAllResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateActivity(Long id, UpActivityRequest ceActivityRequest) {
        VotesActivity votesActivity = new VotesActivity();

        BeanUtils.copyProperties(ceActivityRequest, votesActivity);
        votesActivity.setId(id);

        votesActivity.setSecretVoteFlag(ceActivityRequest.getSecretVoteFlag() == 1);
        int voteButtonType = Optional.of(iActivityDao.selectById(votesActivity.getId()).getVoteButtonType())
                .orElse(ceActivityRequest.getVoteButtonType());

        if (voteButtonType != ceActivityRequest.getVoteButtonType()) {
            // 当前投票的类型与之前的类型不一样的话，之前的投票数据就要删除掉
            LambdaQueryWrapper<VotesActivityVotes> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
            objectLambdaQueryWrapper.eq(VotesActivityVotes::getActivityId, id);
            iVotesActivityVotesService.remove(objectLambdaQueryWrapper);
        }

        // 修改活动
        iActivityDao.updateById(votesActivity);

        if (votesActivity.getCoverType() == 0 && ceActivityRequest.getImgsCover().length > 0) {
            // 删除活动图片封面
            LambdaQueryWrapper<VotesActivityPicture> pictureQueryWrapper = Wrappers.lambdaQuery();
            pictureQueryWrapper.eq(VotesActivityPicture::getActivityId, id);
            iVotesActivityPictureService.remove(pictureQueryWrapper);

            // 活动图片封面设置
            iVotesActivityPictureService.insertActivityPictures(ceActivityRequest.getImgsCover(), votesActivity.getId());
        }
    }

    @Override
    public void addViews(Long id) {
        UpdateWrapper<VotesActivity> updateWrapper = Wrappers.update();
        updateWrapper.setSql("views = views + 1")
                .eq("id", id);

        iActivityDao.update(null, updateWrapper);
    }

    @Override
    public IPage<ActivitySortResponse> getActivitySort(Long activityId, Long groupId) {
        VotesActivity votesActivity = iActivityDao.selectById(activityId);
        Integer voteButtonType = votesActivity.getVoteButtonType();

        IPage<VotesActivity> votesActivityIPage = new Page<>(1,100);
        QueryWrapper<VotesActivity> query = Wrappers.query();

        query.eq("va.id", activityId)
                .eq(groupId != -1,"sua.group_id ", groupId)
                .eq("sua.review", 1)
                .orderByDesc(voteButtonType == VoteButtonTypeEnum.THREE_TYPE.getValue(), "averageScore")
        ;

        IPage<ActivitySortResponse> activitySortResponseIPage = iActivityDao.selectActivitySortPage(votesActivityIPage, query);
        if (voteButtonType == VoteButtonTypeEnum.THREE_TYPE.getValue()) {
            System.out.println(activitySortResponseIPage.getRecords());

            activitySortResponseIPage.getRecords()
                    .sort((o1, o2) -> (int) (o2.getAverageScore() - o1.getAverageScore()));
        } else {
            activitySortResponseIPage.getRecords().sort((o1, o2) -> (int) (o2.getVoteCount() - o1.getVoteCount()));
        }
        return activitySortResponseIPage;
    }

    @Override
    public IPage<ActivityInfoPartResponse> getInfoPageAll(BasePageRequest basePageRequest) {
        IPage<VotesActivity> votesActivityIPage = new Page<>(basePageRequest.getCurrentPage(), basePageRequest.getLimit());
        QueryWrapper<VotesActivity> query = Wrappers.query();

        return iActivityDao.selectInfoPartPage(votesActivityIPage,query );
    }
}
