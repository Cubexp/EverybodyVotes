package com.zj.everybodyvotes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zj.everybodyvotes.domain.VotesActivityPicture;

/**
 * @author cuberxp
 * @date 2021/5/14 10:38 下午
 */
public interface IVotesActivityPictureService extends IService<VotesActivityPicture> {
    void insertActivityPictures(String[] coverImages, Long id);
}
