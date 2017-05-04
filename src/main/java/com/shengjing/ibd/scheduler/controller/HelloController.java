package com.shengjing.ibd.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by gongye1 on 2017/5/2.
 */
@Controller
public class HelloController {
    @RequestMapping(value = "/hello")
    public String hello(ModelMap modelMap) {
        modelMap.addAttribute("message", "hello,world!");
        return "test";
    }

}
