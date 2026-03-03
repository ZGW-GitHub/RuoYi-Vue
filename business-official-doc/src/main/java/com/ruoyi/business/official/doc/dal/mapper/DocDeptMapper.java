package com.ruoyi.business.official.doc.dal.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.business.common.mybatis.mapper.BaseMapper;
import com.ruoyi.business.official.doc.controller.domain.DocDeptPageReq;
import com.ruoyi.business.official.doc.dal.dos.DocDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 机构表 数据层
 * 
 * @author Snow
 */
@Mapper
public interface DocDeptMapper extends BaseMapper<DocDept> {

    Page<DocDept> page(Page<DocDept> page, @Param("req") DocDeptPageReq req);

}
