package com.zj.everybodyvotes.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zj.everybodyvotes.constant.CommonResponseEnum;
import com.zj.everybodyvotes.constant.RoleEnum;
import com.zj.everybodyvotes.dao.IActivityDao;
import com.zj.everybodyvotes.dao.ISysUserActivityDao;
import com.zj.everybodyvotes.dao.ISysUserDao;
import com.zj.everybodyvotes.domain.SysUser;
import com.zj.everybodyvotes.domain.SysUserActivity;
import com.zj.everybodyvotes.domain.request.ActivityPlayerPageRequest;
import com.zj.everybodyvotes.domain.request.CePlayerRequest;
import com.zj.everybodyvotes.domain.response.ActivityPlayerExcelResponse;
import com.zj.everybodyvotes.domain.response.ActivityPlayerResponse;
import com.zj.everybodyvotes.exception.ActivityException;
import com.zj.everybodyvotes.service.ISysUserActivityService;
import com.zj.everybodyvotes.utils.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户参加活动记录
 * @author cuberxp
 * @date 2021/5/14 10:39 下午
 */
@Service
@AllArgsConstructor
public class SysUserActivityServiceImpl extends ServiceImpl<ISysUserActivityDao, SysUserActivity> implements ISysUserActivityService {

    private IActivityDao iActivityDao;

    private final ISysUserActivityDao iSysUserActivityDao;

    private final ISysUserDao iSysUserDao;

    private final SymmetricCrypto symmetricCrypto;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void adminCreatePlayer(Long activityId, CePlayerRequest cePlayerRequest) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(SysUser::getPhone, cePlayerRequest.getPhone());

        boolean flag = iSysUserDao.selectCount(queryWrapper) > 0;

        // 如果flag=true表明，这个账户己经创建过了，直接调用这个账户的信息参加活动就可
        // 如果没有，那就创建这个用户，然后再参加这个活动
        SysUser sysUser = null;
        if (!flag) {
            sysUser = new SysUser()
                    .setPhone(cePlayerRequest.getPhone())
                    .setName(cePlayerRequest.getName())
                    .setRoleName(RoleEnum.USER.getRoleName())
                    .setPassword(symmetricCrypto.encryptBase64(CommonResponseEnum.DEFAULT_PASSWORD.getMessage()))
                    .setAvatar(cePlayerRequest.getImgUrl())
                    .setRegisterTime(DateUtil.localDateTimeToStampTime(LocalDateTime.now()) * 1000)
                    .setGender(1);

            iSysUserDao.insert(sysUser);
        } else {
            sysUser = iSysUserDao.selectOne(queryWrapper);

            // 判断下这个用户有没有参加过这个活动
            LambdaQueryWrapper<SysUserActivity> objectLambdaQueryWrapper = Wrappers.lambdaQuery();
            objectLambdaQueryWrapper
                    .eq(SysUserActivity::getActivityId, activityId)
                    .eq(SysUserActivity::getUserId, sysUser.getId());
            int countCurrentPlayer = iSysUserActivityDao.selectCount(objectLambdaQueryWrapper);

            if (countCurrentPlayer > 0) {
                throw new ActivityException(403, "你己经报过名了哦!!!");
            }
        }

        long playerNumber = Optional.ofNullable(iSysUserActivityDao.selectNumber(activityId))
                .orElse(0L) + 1;

        SysUserActivity sysUserActivity = new SysUserActivity()
                .setActivityId(activityId)
                .setUserId(sysUser.getId())
                .setCreateTime(DateUtil.localDateTimeToStampTime(LocalDateTime.now()) * 1000)
                .setImgUrl(cePlayerRequest.getImgUrl())
                .setGroupId(cePlayerRequest.getGroupId())
                .setNumber(playerNumber)
                .setViews(0L)
                // 由于是主办方添加的默认不需要审核
                .setReview(true)
                .setRemark(cePlayerRequest.getRemark());
        iSysUserActivityDao.insert(sysUserActivity);
    }

    @Override
    public IPage<ActivityPlayerResponse> selectSysUserPlayerPage(ActivityPlayerPageRequest activityPlayerPageRequest) {

        IPage<SysUserActivity> page = new Page<>(activityPlayerPageRequest.getCurrentPage(), activityPlayerPageRequest.getLimit());

        QueryWrapper<SysUserActivity> query = Wrappers.query();

        query.eq("sua.activity_id", activityPlayerPageRequest.getActivityId())
                .eq(activityPlayerPageRequest.getStatus() != null , "sua.review", activityPlayerPageRequest.getStatus())
                // 要用参赛选手分组id 来确定唯一的id
                .eq(activityPlayerPageRequest.getGroupId() != -1, "vag.id", activityPlayerPageRequest.getGroupId())
                .eq(activityPlayerPageRequest.getGroupId() != -1, "sua.group_id", activityPlayerPageRequest.getGroupId());
        return iSysUserActivityDao.selectSysUserActivityPage(page, query);
    }

    @Override
    public void exportPlayer(HttpServletResponse response, Long activityId) throws IOException {
        try {

            List<ActivityPlayerExcelResponse> activityPlayerExcelResponses = iSysUserActivityDao.exportList(activityId);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + "player" + ".xlsx");

            EasyExcel.write(response.getOutputStream(), ActivityPlayerExcelResponse.class)
                    .autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(activityPlayerExcelResponses);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>(2);
            map.put("status", "403");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    @Override
    public Long getMaxNumber(Long activityId) {
        return iSysUserActivityDao.selectNumber(activityId);
    }
}
