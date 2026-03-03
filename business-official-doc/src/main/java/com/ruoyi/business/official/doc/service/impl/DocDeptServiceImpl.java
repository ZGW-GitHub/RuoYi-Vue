package com.ruoyi.business.official.doc.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.common.domain.req.IdsReq;
import com.ruoyi.business.common.domain.resp.PageResp;
import com.ruoyi.business.common.util.BeanUtil;
import com.ruoyi.business.official.doc.controller.domain.DocDeptPageReq;
import com.ruoyi.business.official.doc.controller.domain.DocDeptResp;
import com.ruoyi.business.official.doc.dal.dos.DocDept;
import com.ruoyi.business.official.doc.dal.dos.DocDeptType;
import com.ruoyi.business.official.doc.dal.mapper.DocDeptMapper;
import com.ruoyi.business.official.doc.dal.mapper.DocDeptTypeMapper;
import com.ruoyi.business.official.doc.service.DocDeptService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 机构表 服务实现
 *
 * @author Snow
 */
@Service
public class DocDeptServiceImpl extends ServiceImpl<DocDeptMapper, DocDept> implements DocDeptService {

    @Resource
    private DocDeptMapper docDeptMapper;

    @Resource
    private DocDeptTypeMapper docDeptTypeMapper;

    /**
     * 页面
     *
     * @param req req
     * @return {@link PageResp }<{@link DocDeptResp }>
     */
    @Override
    public PageResp<DocDeptResp> page(DocDeptPageReq req) {
        Page<DocDept> page = docDeptMapper.page(req.mybatisPage(), req);
        PageResp<DocDeptResp> pageResp = PageResp.of(page.getTotal(), BeanUtil.mapList(page.getRecords(), DocDeptResp.class));
        if (CollUtil.isEmpty(pageResp.getRecords())) {
            return pageResp;
        }

        List<Long> deptTypeList = pageResp.getRecords().stream().map(DocDept::getDeptType).distinct().toList();
        if (CollUtil.isEmpty(deptTypeList)) {
            return pageResp;
        }

        List<DocDeptType> deptTypeInfoList = docDeptTypeMapper.selectByIds(deptTypeList);
        Map<Long, DocDeptType> deptTypeMap = deptTypeInfoList.stream().collect(Collectors.toMap(DocDeptType::getId, Function.identity()));
        pageResp.getRecords().forEach(resp -> {
            if (resp.getDeptType() == null) {
                return;
            }

            DocDeptType deptTypeInfo = deptTypeMap.get(resp.getDeptType());
            resp.setDeptTypeInfo(deptTypeInfo);
        });

        return pageResp;
    }


    /**
     * 更新机构类型
     *
     * @param id       机构ID
     * @param deptType 机构类型
     */
    @Override
    public void updateType(Long id, Long deptType) {
        DocDept docDept = new DocDept();
        docDept.setId(id);
        docDept.setDeptType(deptType);
        docDeptMapper.updateById(docDept);
    }

    /**
     * 删除
     *
     * @param req req
     */
    @Override
    public void delete(IdsReq req) {
        docDeptMapper.deleteByIds(req.getIds());
    }

}
