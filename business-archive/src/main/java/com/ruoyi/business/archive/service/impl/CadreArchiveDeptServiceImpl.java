package com.ruoyi.business.archive.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.archive.controller.domain.CadreArchiveDeptSaveReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveDeptTreeItem;
import com.ruoyi.business.archive.dal.dos.CadreArchiveDept;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveDeptMapper;
import com.ruoyi.business.archive.dal.mapper.CadreArchiveMapper;
import com.ruoyi.business.archive.service.CadreArchiveDeptService;
import com.ruoyi.business.common.domain.req.IdsReq;
import com.ruoyi.business.common.util.BeanUtil;
import com.ruoyi.business.common.util.TreeUtil;
import com.ruoyi.business.common.web.exception.BizException;
import com.ruoyi.business.common.web.exception.code.BizExceptionCode;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * 干部档案部门表 服务实现
 *
 * @author Snow
 */
@Service
public class CadreArchiveDeptServiceImpl extends ServiceImpl<CadreArchiveDeptMapper, CadreArchiveDept> implements CadreArchiveDeptService {

    @Resource
    private CadreArchiveMapper cadreArchiveMapper;

    @Resource
    private CadreArchiveDeptMapper cadreArchiveDeptMapper;

    /**
     * 树
     *
     * @return {@link List }<{@link CadreArchiveDeptTreeItem }>
     */
    @Override
    public List<CadreArchiveDeptTreeItem> tree() {
        List<CadreArchiveDept> deptList = cadreArchiveDeptMapper.selectAll();
        if (CollUtil.isEmpty(deptList)) {
            return Collections.emptyList();
        }

        List<CadreArchiveDeptTreeItem> treeItemList = BeanUtil.mapList(deptList, CadreArchiveDeptTreeItem.class, (source, target) -> {
            target.setKey(String.valueOf(source.getId()));
            target.setTitle(source.getDeptName());
            target.setParentKey(String.valueOf(source.getParentId()));
            target.setAncestorsKey(source.getAncestors());
        });

        return TreeUtil.buildTree(treeItemList);
    }

    /**
     * 保存部门
     *
     * @param req 保存请求
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(CadreArchiveDeptSaveReq req) {
        if (req.getId() == null) {
            insert(req);
        } else {
            update(req);
        }
    }

    private void insert(CadreArchiveDeptSaveReq req) {
        Long reqParentId = req.getParentId();

        // 创建
        CadreArchiveDept dept = new CadreArchiveDept();
        dept.setDeptName(req.getDeptName());
        dept.setParentId(reqParentId);
        dept.setSort(req.getSort());

        // 计算 ancestors
        String ancestors = calcAncestors(reqParentId);
        dept.setAncestors(ancestors);

        // 执行数据库
        cadreArchiveDeptMapper.insert(dept);
    }

    private void update(CadreArchiveDeptSaveReq req) {
        Long reqId = req.getId();
        Long reqParentId = req.getParentId();

        CadreArchiveDept dept = getById(reqId);
        if (dept == null) {
            throw new BizException(BizExceptionCode.PARAMS_ERROR, "单位不存在");
        }

        // 检查循环引用
        if (reqParentId.equals(reqId)) {
            throw new BizException(BizExceptionCode.PARAMS_ERROR, "不能将单位设置为自己的子单位");
        }

        // 检查是否设置为子孙部门
        List<CadreArchiveDept> descendantDeptList = cadreArchiveDeptMapper.listDescendant(reqId);
        List<Long> descendantDeptIdList = descendantDeptList.stream().map(CadreArchiveDept::getId).toList();
        if (descendantDeptIdList.contains(reqParentId)) {
            throw new BizException(BizExceptionCode.PARAMS_ERROR, "不能将单位设置为自己的子单位");
        }

        // 保存旧的 ancestors 用于级联更新
        Long oldParentId = dept.getParentId();
        String oldAncestors = dept.getAncestors();

        // 更新部门信息
        dept.setDeptName(req.getDeptName());
        dept.setParentId(reqParentId);
        dept.setSort(req.getSort());

        // 如果父部门变更，重新计算 ancestors
        if (!isSameParent(oldParentId, reqParentId)) {
            String newAncestors = calcAncestors(reqParentId);
            dept.setAncestors(newAncestors);

            // 级联更新所有子孙部门的 ancestors
            updateDescendantAncestors(reqId, descendantDeptList, oldAncestors, newAncestors);
        }

        // 执行数据库
        cadreArchiveDeptMapper.updateById(dept);
    }

    /**
     * 批量删除部门
     *
     * @param req req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(IdsReq req) {
        List<Long> idList = req.ids();

        idList.forEach(id -> {
            // 检查子部门
            Long childCount = cadreArchiveDeptMapper.countChildren(id);
            if (childCount > 0) {
                throw new BizException(BizExceptionCode.PARAMS_ERROR, "存在子单位，不允许删除！");
            }

            // 检查关联档案
            Long archiveCount = cadreArchiveMapper.countByDeptId(id);
            if (archiveCount > 0) {
                throw new BizException(BizExceptionCode.PARAMS_ERROR, "单位下存在档案数据，不允许删除！");
            }

            // 执行删除
            boolean success = removeById(id);
            if (!success) {
                throw new BizException(BizExceptionCode.PARAMS_ERROR, "删除失败！");
            }
        });

        cadreArchiveMapper.updateDeptId(idList);
    }

    /**
     * 计算 ancestors 字段
     */
    private String calcAncestors(Long parentId) {
        if (parentId == null || parentId == 0) {
            return "";
        }

        CadreArchiveDept parentDept = cadreArchiveDeptMapper.selectById(parentId);
        if (parentDept == null) {
            throw new BizException(BizExceptionCode.PARAMS_ERROR, "父单位不存在");
        }

        if (StrUtil.isBlank(parentDept.getAncestors())) {
            return String.valueOf(parentId);
        } else {
            return parentDept.getAncestors() + "," + parentId;
        }
    }

    /**
     * 级联更新子孙部门的 ancestors
     */
    private void updateDescendantAncestors(Long deptId, List<CadreArchiveDept> descendantDeptList, String oldAncestors, String newAncestors) {
        if (descendantDeptList.isEmpty()) {
            return;
        }

        // 构建旧的路径前缀
        String oldPrefix = StrUtil.isBlank(oldAncestors) ? String.valueOf(deptId) : oldAncestors + "," + deptId;

        // 构建新的路径前缀
        String newPrefix = StrUtil.isBlank(newAncestors) ? String.valueOf(deptId) : newAncestors + "," + deptId;

        // 更新每个子孙部门的 ancestors
        for (CadreArchiveDept descendant : descendantDeptList) {
            String oldDescendantAncestors = descendant.getAncestors();
            String newDescendantAncestors = oldDescendantAncestors.replaceFirst("^" + oldPrefix.replace(",", "\\,"), newPrefix);
            descendant.setAncestors(newDescendantAncestors);
        }

        // 批量更新
        cadreArchiveDeptMapper.updateById(descendantDeptList);
    }

    /**
     * 判断父部门是否相同
     */
    private boolean isSameParent(Long oldParentId, Long newParentId) {
        if (oldParentId == null && newParentId == null) {
            return true;
        }
        if (oldParentId == null || newParentId == null) {
            return false;
        }
        return oldParentId.equals(newParentId);
    }

}
