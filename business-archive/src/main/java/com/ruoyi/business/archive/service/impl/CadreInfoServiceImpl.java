package com.ruoyi.business.archive.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.archive.dal.dos.CadreInfo;
import com.ruoyi.business.archive.dal.mapper.CadreInfoMapper;
import com.ruoyi.business.archive.service.CadreInfoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 干部信息 Service 实现类
 * 
 * @author Snow
 */
@Service
public class CadreInfoServiceImpl extends ServiceImpl<CadreInfoMapper, CadreInfo> implements CadreInfoService {

    @Resource
    private CadreInfoMapper cadreInfoMapper;

}