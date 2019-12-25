package com.github.hicolors.swagger.apiui.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController{
	
    @RequestMapping(value = "/check", method = {RequestMethod.GET, RequestMethod.HEAD})
    public Boolean check() {
        return true;
    }

}
