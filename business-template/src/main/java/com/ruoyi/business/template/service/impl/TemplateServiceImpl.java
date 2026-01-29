package com.ruoyi.business.template.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.template.dal.dos.Template;
import com.ruoyi.business.template.dal.mapper.TemplateMapper;
import com.ruoyi.business.template.service.TemplateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author Snow
 */
@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, Template> implements TemplateService {

    @Resource
    private TemplateMapper templateMapper;

}
