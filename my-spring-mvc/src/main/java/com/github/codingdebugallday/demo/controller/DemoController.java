package com.github.codingdebugallday.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.codingdebugallday.demo.service.DemoService;
import com.github.codingdebugallday.mvcframework.annotations.AutoWired;
import com.github.codingdebugallday.mvcframework.annotations.Controller;
import com.github.codingdebugallday.mvcframework.annotations.RequestMapping;
import com.github.codingdebugallday.mvcframework.annotations.Security;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/13 21:05
 * @since 1.0.0
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @AutoWired
    private DemoService demoService;

    @RequestMapping("/query01")
    @Security({"zhangsan"})
    public String query01(HttpServletRequest request, HttpServletResponse response, String username) {
        return demoService.get(username);
    }

    @RequestMapping("/query02")
    @Security({"lisi"})
    public String query02(HttpServletRequest request, HttpServletResponse response, String username) {
        return demoService.get(username);
    }
}
