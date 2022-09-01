package com.zj.everybodyvotes.domain.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回活动链接与二维码
 * @author cuberxp
 * @date 2021/5/14 11:58 下午
 */
@Data
@Accessors(chain = true)
public class ActivityQRCodeResponse {
    /**
     * 活动二维码
     */
    private String activityQRCode;

    /**
     * 活动链接
     */
    private String activityUrl;
}
