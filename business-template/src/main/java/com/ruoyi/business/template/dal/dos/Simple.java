package com.ruoyi.business.template.dal.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.business.common.mybatis.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author Snow
 */
@Data
@TableName("bus_simple")
@EqualsAndHashCode(callSuper = true)
public class Simple extends BaseDO {

    @Serial
    private static final long serialVersionUID = 4891794967970774601L;

}
