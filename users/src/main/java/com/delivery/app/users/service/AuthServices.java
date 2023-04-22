package com.delivery.app.users.service;

import com.delivery.app.users.dto.AuthRequest;
import com.delivery.app.users.dto.AuthenticationResponse;
import com.delivery.app.users.dto.RegisterRequest;
import com.delivery.app.users.email.EmailSender;
import com.delivery.app.users.entity.Role;
import com.delivery.app.users.entity.User;
import com.delivery.app.users.entity.token.VerificationToken;
import com.delivery.app.users.entity.token.VerificationTokenService;
import com.delivery.app.users.repository.UserRepository;
import com.delivery.app.users.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServices {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailSender emailSender;

    private final VerificationTokenService verificationTokenService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws MessagingException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return AuthenticationResponse.builder()
                    .message("User already exists")
                    .build();
        }

        var user = User
                .builder()
                .fullName(request.getFullName())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();
        userRepository.save(user);

        String verificationToken = verificationTokenService.generateVerificationToken(user.getUsername());
        String link = "http://localhost:8084/api/v1/auth/validateAccount/"+ verificationToken;
        emailSender.sendEmailVerification(request.getEmail() , createHtmlEmail(request.getFullName() , link ) );
        return AuthenticationResponse
                .builder()
                .message("user created successfully , you need to verify account")
                .build();
    }


    public AuthenticationResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().message(jwtToken).message(jwtToken).build();
    }

    public String validateUserAccount(String token){
        VerificationToken verificationToken = verificationTokenService.getToken(token).orElseThrow(()->new IllegalAccessError("token not found"));
        String username = verificationToken.getUsername();
        User user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
        user.setEnabled(true);
        userRepository.save(user);
        return "account confirmed";
    }
    public String createHtmlEmail(String name, String link) {
        String html = "<html>" +
                "<head>" +
                "<style type=\"text/css\">" +
                "/* Your CSS code here */" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<p>Dear " + name + ",</p>" +
                "<p>Please click on the link below to verify your account:</p>" +
                "<a href='" + link + "'>Verify Account</a>" +
                "</body>" +
                "</html>";
        return html;
    }

}
