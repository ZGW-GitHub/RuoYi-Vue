package com.ruoyi.web.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Snow
 */
@Controller
public class FrontendController {

    /**
     * 匹配所有前端路由（ 避免 404 ）
     */
    @GetMapping(value = {"/", "/login", "/home", "/menu/**"})
    public String forward() {
        return "forward:/index.html";
    }

}
