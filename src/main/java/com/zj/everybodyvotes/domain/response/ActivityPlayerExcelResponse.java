package com.zj.everybodyvotes.domain.response;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author cuberxp
 * @date 2021/5/16 9:40 上午
 */
@Data
public class ActivityPlayerExcelResponse {
    @ExcelProperty("名称")
    private String name;

    @ColumnWidth(15)
    @ExcelProperty("选手编号")
    private Long number;

    @ExcelProperty("浏览量")
    private Long views;

    /**
     * 审核是否通过
     */
    @ColumnWidth(20)
    @ExcelProperty("审核是否通过")
    private Boolean review;

    /**
     * 获取选手投票数
     */
    @ExcelProperty("票数")
    private Long votesCount;
}