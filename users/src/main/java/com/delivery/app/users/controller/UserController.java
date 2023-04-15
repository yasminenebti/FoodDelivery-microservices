package com.delivery.app.users.controller;

import com.delivery.app.users.dto.AuthRequest;
import com.delivery.app.users.dto.AuthenticationResponse;
import com.delivery.app.users.dto.RegisterRequest;
import com.delivery.app.users.service.AuthServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthServices authServices;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authServices.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthRequest request)
    {
        return ResponseEntity.ok(authServices.authenticate(request));
    }
}
