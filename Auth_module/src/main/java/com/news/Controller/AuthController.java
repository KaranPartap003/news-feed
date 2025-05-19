package com.news.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/users")
    public String getUsers(){
        return "users list";
    }



}
