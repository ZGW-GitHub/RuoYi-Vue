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
import com.ruoyi.business.official.doc.dal.dos.DocDeptGroup;
import com.ruoyi.business.official.doc.dal.mapper.DocDeptGroupMapper;
import com.ruoyi.business.official.doc.dal.mapper.DocDeptMapper;
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
    private DocDeptGroupMapper docDeptGroupMapper;

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

        List<Long> deptGroupList = pageResp.getRecords().stream().map(DocDept::getDeptGroup).distinct().toList();
        if (CollUtil.isEmpty(deptGroupList)) {
            return pageResp;
        }

        List<DocDeptGroup> deptGroupInfoList = docDeptGroupMapper.selectByIds(deptGroupList);
        Map<Long, DocDeptGroup> deptGroupMap = deptGroupInfoList.stream().collect(Collectors.toMap(DocDeptGroup::getId, Function.identity()));
        pageResp.getRecords().forEach(resp -> {
            if (resp.getDeptGroup() == null) {
                return;
            }

            DocDeptGroup deptGroupInfo = deptGroupMap.get(resp.getDeptGroup());
            resp.setDeptGroupInfo(deptGroupInfo);
        });

        return pageResp;
    }


    /**
     * 更新机构类型
     *
     * @param id       机构ID
     * @param deptGroup 机构类型
     */
    @Override
    public void updateGroup(Long id, Long deptGroup) {
        DocDept docDept = new DocDept();
        docDept.setId(id);
        docDept.setDeptGroup(deptGroup);
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
