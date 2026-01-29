package com.ruoyi.business.archive.controller;

import com.ruoyi.business.archive.service.CadreInfoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 干部信息 Controller
 * 
 * @author Snow
 */
@RestController
@RequestMapping("cadre")
public class CadreInfoController {

    @Resource
    private CadreInfoService cadreInfoService;

}