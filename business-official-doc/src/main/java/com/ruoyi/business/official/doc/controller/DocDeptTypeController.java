package com.ruoyi.business.official.doc.controller;

import com.ruoyi.business.official.doc.controller.domain.DocDeptTypeTreeItem;
import com.ruoyi.business.official.doc.service.DocDeptTypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 机构类型表 控制层
 *
 * @author Snow
 */
@RestController
@RequestMapping("business/official/docDeptType")
public class DocDeptTypeController {

    @Resource
    private DocDeptTypeService docDeptTypeService;

    @GetMapping("tree")
    public List<DocDeptTypeTreeItem> tree() {
        return docDeptTypeService.tree();
    }

}
