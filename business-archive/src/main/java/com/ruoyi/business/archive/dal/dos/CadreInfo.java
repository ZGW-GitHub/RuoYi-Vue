package com.ruoyi.business.archive.dal.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.business.common.mybatis.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 干部信息表
 * 
 * @author Snow
 */
@Data
@TableName("bus_cadre_info")
@EqualsAndHashCode(callSuper = true)
public class CadreInfo extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 干部姓名
     */
    private String cadresName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 出生年月
     */
    private String birthday;

    /**
     * 民族
     */
    private String ethnic;

    /**
     * 部门ID
     */
    private Long deptId;

}