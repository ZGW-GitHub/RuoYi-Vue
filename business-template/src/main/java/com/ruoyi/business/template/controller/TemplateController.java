package com.ruoyi.business.template.controller;

import com.ruoyi.business.template.service.TemplateService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Snow
 */
@RestController
@RequestMapping("template")
public class TemplateController {

    @Resource
    private TemplateService templateService;


}
