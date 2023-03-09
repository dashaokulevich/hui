package ru.kata.spring.boot_security.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllers {

    @GetMapping("/")
    public String homePage(){
        return "index";
    }
    @GetMapping("/authenticated")
    public String pageForAuthenticatedUser(){
        return "secured part of web services";
    }

}
