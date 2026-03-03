package com.ruoyi.business.official.doc.controller;

import com.ruoyi.business.common.domain.req.IdsReq;
import com.ruoyi.business.common.domain.resp.PageResp;
import com.ruoyi.business.official.doc.controller.domain.DocDeptPageReq;
import com.ruoyi.business.official.doc.controller.domain.DocDeptResp;
import com.ruoyi.business.official.doc.service.DocDeptService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

/**
 * 机构表 控制层
 *
 * @author Snow
 */
@RestController
@RequestMapping("business/official/docDept")
public class DocDeptController {

    @Resource
    private DocDeptService docDeptService;

    @GetMapping("page")
    public PageResp<DocDeptResp> page(DocDeptPageReq req) {
        return docDeptService.page(req);
    }

    @PutMapping("updateType")
    public void updateType(@RequestParam Long id, @RequestParam @NotNull Long deptType) {
        docDeptService.updateType(id, deptType);
    }

    @DeleteMapping("delete")
    public void delete(@Valid @RequestBody IdsReq req) {
        docDeptService.delete(req);
    }

}
