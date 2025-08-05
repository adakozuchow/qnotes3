package ydgrun.info.qnotes3.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ydgrun.info.qnotes3.api.ApiApi;
import ydgrun.info.qnotes3.model.AuthResponse;
import ydgrun.info.qnotes3.model.LoginRequest;
import ydgrun.info.qnotes3.model.RegisterRequest;

@RestController
public class AuthenticationController implements ApiApi {

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        // TODO: Implement authentication logic
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
        // TODO: Implement registration logic
        throw new UnsupportedOperationException("Not implemented yet");
    }
}