package org.example.but_eo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/addmin")
@Controller
public class Addmincontroller {
    @GetMapping
    public String login(){
        return "index";
    }
}
