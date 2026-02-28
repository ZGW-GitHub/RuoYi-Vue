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
    @SuppressWarnings("all")
    @GetMapping(value = {
            "/", "/login", "/home", "/menu/**",
            "/{path:[^.]*}", "/**/{path:[^.]*}" // 不包含点号（作用：只拦截前端路由）
    })
    public String forward() {
        return "forward:/index.html";
    }

}
