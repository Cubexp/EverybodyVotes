package com.zj.everybodyvotes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zj.everybodyvotes.dao.IVotesActivityPictureDao;
import com.zj.everybodyvotes.domain.VotesActivityPicture;
import com.zj.everybodyvotes.service.IVotesActivityPictureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 活动封面图片集
 * @author cuberxp
 * @date 2021/5/14 10:41 下午
 */
@Service
public class VotesActivityPictureServiceImpl extends ServiceImpl<IVotesActivityPictureDao, VotesActivityPicture> implements IVotesActivityPictureService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertActivityPictures(String[] coverImages, Long activityId) {
        System.out.println(Arrays.toString(coverImages));
        List<VotesActivityPicture> collect = Arrays.stream(coverImages)
                .filter(Objects::nonNull)
                .map(imgUrl -> new VotesActivityPicture()
                        .setActivityId(activityId)
                        .setImgUrl(imgUrl)).collect(Collectors.toList());

        if (collect.size() > 0) this.saveBatch(collect, collect.size());
    }
}
