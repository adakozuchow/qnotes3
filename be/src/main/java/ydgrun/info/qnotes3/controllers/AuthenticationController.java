package ydgrun.info.qnotes3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ydgrun.info.qnotes3.api.AuthenticationApi;
import ydgrun.info.qnotes3.model.AuthResponse;
import ydgrun.info.qnotes3.model.LoginRequest;
import ydgrun.info.qnotes3.model.RegisterRequest;

@RestController
public class AuthenticationController implements AuthenticationApi {

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        AuthResponse response = new AuthResponse();
        response.setToken("dummy.jwt.token-" + loginRequest.getUsername());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
        AuthResponse response = new AuthResponse();
        response.setToken("dummy.jwt.token-" + registerRequest.getUsername());
        return ResponseEntity.status(201).body(response);
    }
}