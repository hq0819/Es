package com.hq.es.controller;

import com.hq.es.annotations.MyAnnotation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping("/getUserInfo")
    @MyAnnotation(name="heqin")
    public Object getUserInfo(){
        return "hq";
    }
}
