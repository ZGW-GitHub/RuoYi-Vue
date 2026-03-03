package com.ruoyi.business.official.doc.controller;

import com.ruoyi.business.official.doc.controller.domain.DocDeptGroupTreeItem;
import com.ruoyi.business.official.doc.service.DocDeptGroupService;
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
@RequestMapping("business/official/docDeptGroup")
public class DocDeptGroupController {

    @Resource
    private DocDeptGroupService docDeptGroupService;

    @GetMapping("tree")
    public List<DocDeptGroupTreeItem> tree() {
        return docDeptGroupService.tree();
    }

}
