package com.zj.everybodyvotes.utils;

import com.zj.everybodyvotes.constant.ImgTypeConstant;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author cuberxp
 * @date 2021/5/10 12:03 下午
 */
public class FileTypeUtil {

    public static final String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\/\\.\\(\\)\\·\\u4e00-\\u9fa5]+";

    /**
     * 判断文件类型
     *
     * 每种文件类型的十六进进制文件头(前28个字节)是特定的。
     * 如jpg 他的十六进制头是 FFD8FF******
     * 根据这个我们来判断他文件类型
     *
     * @param file 上传的文件封装类
     * @return {@link ImgTypeConstant}
     */
    public static ImgTypeConstant getType(MultipartFile file) throws IOException {
        // 获取文件头
        String fileHead = getFileHeader(file);

        if (fileHead != null && fileHead.length() > 0) {
            fileHead = fileHead.toUpperCase();
            ImgTypeConstant[] types = ImgTypeConstant.values();

            for (ImgTypeConstant type : types) {
                if (fileHead.startsWith(type.getValue())) {
                    return type;
                }
            }
        }

        return null;
    }

    /**
     * 读取文件头信息
     * @param file 上传的文件封装类
     * @return 文件
    */
    private static String getFileHeader(MultipartFile file) throws IOException {
        byte[] b = new byte[28];
        InputStream inputStream = file.getInputStream();
        inputStream.read(b, 0, 28);
        return bytesToHex(b);
    }

    /**
     * 将字节数组转换成16进制字符串
     * @param src File的二进制数据中前28个字节
     * @return 16进制字符串
     */
    private static String bytesToHex(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
