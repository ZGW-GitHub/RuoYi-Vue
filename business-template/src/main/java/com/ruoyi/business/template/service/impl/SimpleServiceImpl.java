package com.ruoyi.business.template.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.template.dal.dos.Simple;
import com.ruoyi.business.template.dal.mapper.SimpleMapper;
import com.ruoyi.business.template.service.SimpleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author Snow
 */
@Service
public class SimpleServiceImpl extends ServiceImpl<SimpleMapper, Simple> implements SimpleService {

    @Resource
    private SimpleMapper simpleMapper;

}
