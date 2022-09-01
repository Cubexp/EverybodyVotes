package com.zj.everybodyvotes.service.impl;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.bean.base.BaseResponseBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import com.zj.everybodyvotes.constant.UCloudProperties;
import com.zj.everybodyvotes.service.IUCloudService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

/**
 * @author cuberxp
 * @date 2021/5/10 12:11 下午
 */
@Service
@AllArgsConstructor
public class UCloudServiceImpl implements IUCloudService {

    private final ObjectAuthorization objectAuthorization;

    private final ObjectConfig objectConfig;

    @Override
    public String getEnclosure(String generateFileName, int expiresDuration) throws UfileClientException {
        return UfileClient.object(objectAuthorization, objectConfig)
                //单位秒
                .getDownloadUrlFromPrivateBucket(generateFileName, UCloudProperties.BUCKET_NAME, expiresDuration)
                .createUrl();
    }   

    @Override
    public PutObjectResultBean uploadEnclosure(InputStream inputStream, String contentType, String generateFileName) throws UfileServerException, UfileClientException {
        PutObjectResultBean putObjectResultBean = Optional.ofNullable(UfileClient.object(objectAuthorization, objectConfig)
                .putObject(inputStream, contentType)
                .nameAs(generateFileName)
                .toBucket(UCloudProperties.BUCKET_NAME)
                .execute())
                .orElseThrow(() -> new UfileServerException("文件上传失败!"));

        if(putObjectResultBean.getRetCode() == 0) {
            return putObjectResultBean;
        }else {
            throw new UfileServerException("文件上传失败!" + putObjectResultBean.getMessage());
        }
    }

    @Override
    public BaseResponseBean deleteEnclosure(String keyName) throws UfileServerException, UfileClientException {
        return UfileClient.object(objectAuthorization, objectConfig)
                .deleteObject(keyName, UCloudProperties.BUCKET_NAME)
                .execute();
    }
}
