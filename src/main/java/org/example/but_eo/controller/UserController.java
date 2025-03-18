package org.example.but_eo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Hello Wolrd";
    }
}
