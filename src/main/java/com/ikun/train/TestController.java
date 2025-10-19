package com.ikun.train;

import org.springframework.web.bind.annotation.GetMapping;

public class TestController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello world11wqe1";
    }
}

