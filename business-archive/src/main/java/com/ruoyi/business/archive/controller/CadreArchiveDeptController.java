package com.ruoyi.business.archive.controller;

import com.ruoyi.business.archive.controller.domain.CadreArchiveDeptSaveReq;
import com.ruoyi.business.archive.controller.domain.CadreArchiveDeptTreeItem;
import com.ruoyi.business.archive.service.CadreArchiveDeptService;
import com.ruoyi.business.common.domain.req.IdsReq;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 干部档案部门表 控制层
 *
 * @author Snow
 */
@RestController
@RequestMapping("business/archive/archiveDept")
public class CadreArchiveDeptController {

    @Resource
    private CadreArchiveDeptService cadreArchiveDeptService;

    @GetMapping("tree")
    public List<CadreArchiveDeptTreeItem> tree() {
        return cadreArchiveDeptService.tree();
    }

    /**
     * 保存部门
     *
     * @param req 保存请求
     */
    @PostMapping("save")
    public Boolean save(@Valid @RequestBody CadreArchiveDeptSaveReq req) {
        return cadreArchiveDeptService.save(req);
    }

    /**
     * 删除部门
     *
     * @param req 删除请求
     */
    @DeleteMapping("delete")
    public Boolean delete(@Valid @RequestBody IdsReq req) {
        return cadreArchiveDeptService.delete(req);
    }

}
