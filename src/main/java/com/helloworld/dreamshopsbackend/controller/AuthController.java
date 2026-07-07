package com.helloworld.dreamshopsbackend.controller;

import com.helloworld.dreamshopsbackend.request.LoginRequest;
import com.helloworld.dreamshopsbackend.response.ApiResponse;
import com.helloworld.dreamshopsbackend.response.JwtResponse;
import com.helloworld.dreamshopsbackend.security.jwt.JwtUtils;
import com.helloworld.dreamshopsbackend.security.user.ShopUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${auth-api.prefix}")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateTokenForUser(authentication);
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            JwtResponse response = new JwtResponse(userDetails.getId(), jwt);
            return ResponseEntity.ok(
                    new ApiResponse(
                            "Successfully Login",
                            response
                    )
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse(
                            "Invalid email or password",
                            e.getMessage()
                    )
            );
        }
    }
}
