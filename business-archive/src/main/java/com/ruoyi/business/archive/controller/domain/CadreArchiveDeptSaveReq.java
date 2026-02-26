package com.ruoyi.business.archive.controller.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 保存干部档案部门请求对象
 * 
 * @author Snow
 */
@Data
public class CadreArchiveDeptSaveReq {

    private Long id;

    @NotBlank(message = "单位名称不能为空")
    @Length(max = 50, message = "单位名称长度不能超过50个字符")
    private String deptName;

    @NotNull(message = "上级单位不能为空")
    private Long parentId;

    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序必须为非负整数")
    private Integer sort;

}
