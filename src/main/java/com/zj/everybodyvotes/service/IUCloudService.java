package com.zj.everybodyvotes.service;

import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.bean.base.BaseResponseBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;

import java.io.InputStream;

/**
 * @author cuberxp
 * @date 2021/5/10 12:09 下午
 */
public interface IUCloudService {
    String getEnclosure(String generateFileName, int i) throws UfileClientException;

    PutObjectResultBean uploadEnclosure(InputStream inputStream, String contentType, String generateFileName) throws UfileServerException, UfileClientException;

    /**
     * 根据文件名删除图片附件
     * @param keyName 文件名(唯一)
     * @return 服务器响应类,用来检查本次操作是否成功
     * @throws UfileServerException 服务器异常
     * @throws UfileClientException 客户端异常
     */
    BaseResponseBean deleteEnclosure(String keyName) throws UfileServerException, UfileClientException;

}
