package com.rohit.reddit.springredditclonebackend.controller;

import com.rohit.reddit.springredditclonebackend.dto.AuthenticationResponse;
import com.rohit.reddit.springredditclonebackend.dto.LoginRequest;
import com.rohit.reddit.springredditclonebackend.dto.RegisterRequest;
import com.rohit.reddit.springredditclonebackend.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
        System.out.println("Inside Signup");
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registation Successfull",
                HttpStatus.OK);
    }
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully!",HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
}
