package com.ruoyi.business.archive.controller;

import com.ruoyi.business.archive.service.CadreArchiveDeptService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
