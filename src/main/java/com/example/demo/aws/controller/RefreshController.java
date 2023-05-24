package com.example.demo.aws.controller;

import com.example.demo.aws.util.Env;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshController {

    @RequestMapping("/refresh")
    public String refresh() {
        Env.setRefreshTime();
        return Env.getRefreshTime().toString();
    }

}
