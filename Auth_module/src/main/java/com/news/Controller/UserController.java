package com.news.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/list")
    public String getUsers(){
        return "users list";
    }

}
