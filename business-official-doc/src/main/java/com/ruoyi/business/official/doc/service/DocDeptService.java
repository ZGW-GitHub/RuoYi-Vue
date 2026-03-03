package com.ruoyi.business.official.doc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.business.common.domain.req.IdsReq;
import com.ruoyi.business.common.domain.resp.PageResp;
import com.ruoyi.business.official.doc.controller.domain.DocDeptPageReq;
import com.ruoyi.business.official.doc.controller.domain.DocDeptResp;
import com.ruoyi.business.official.doc.dal.dos.DocDept;

/**
 * 机构表 服务层
 *
 * @author Snow
 */
public interface DocDeptService extends IService<DocDept> {

    /**
     * 页面
     *
     * @param req req
     * @return {@link PageResp }<{@link DocDeptResp }>
     */
    PageResp<DocDeptResp> page(DocDeptPageReq req);


    /**
     * 更新机构类型
     *
     * @param id       机构ID
     * @param deptType 机构类型
     */
    void updateType(Long id, Long deptType);

    /**
     * 删除
     *
     * @param req req
     */
    void delete(IdsReq req);


}
