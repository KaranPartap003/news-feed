package com.news.Controller;

import com.news.Model.LoginDTO;
import com.news.Model.RegisterDTO;
import com.news.Model.Users;
import com.news.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> signinUser (@RequestBody LoginDTO loginRequest){
        //create user object for authentication
        Users user = new Users(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        String token = userService.verify(user);
        Map<String, String> response = new HashMap<>();
        if(token == null){
            response.put("accessToken", null);
            response.put("message", "user not found, register first");
            ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(response);
        }
        response.put("accessToken", token);
        response.put("message", "logged in successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public Users registerUser (@RequestBody RegisterDTO registerRequest){
        Users user = new Users(registerRequest.getUsername(), registerRequest.getPassword());
        return userService.register(user);
    }

}
