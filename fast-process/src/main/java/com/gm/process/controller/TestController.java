package com.gm.process.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试类
 *
 * @author gongmin
 * @date 2025/10/28 10:16
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "ok";
    }
}
