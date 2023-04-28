package com.delivery.app.users.controller;

import com.delivery.app.users.dto.AuthRequest;
import com.delivery.app.users.dto.AuthenticationResponse;
import com.delivery.app.users.dto.RegisterRequest;
import com.delivery.app.users.service.AuthServices;
import com.delivery.clients.users.UserRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthServices authServices;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) throws MessagingException {
        return ResponseEntity.ok(authServices.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthRequest request)
    {
        return ResponseEntity.ok(authServices.authenticate(request));
    }

    @GetMapping("/validateAccount/{token}")
    public ResponseEntity<String> confirmUserAccount(@PathVariable String token){
        return ResponseEntity.ok(authServices.validateUserAccount(token));
    }


    @GetMapping("/validateToken/{token}")
    public ResponseEntity<UserRequest> validateToken(@PathVariable String token){
        return ResponseEntity.ok(authServices.validateToken(token));
    }

}
