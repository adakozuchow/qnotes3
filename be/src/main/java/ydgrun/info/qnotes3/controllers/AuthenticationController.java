package ydgrun.info.qnotes3.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ydgrun.info.qnotes3.api.AuthenticationApi;
import ydgrun.info.qnotes3.domain.User;
import ydgrun.info.qnotes3.model.AuthResponse;
import ydgrun.info.qnotes3.model.LoginRequest;
import ydgrun.info.qnotes3.model.RegisterRequest;
import ydgrun.info.qnotes3.service.UserService;

@RestController
public class AuthenticationController implements AuthenticationApi {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        logger.debug("Login request for user: {}", loginRequest.getUsername());
        User user = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        
        AuthResponse response = new AuthResponse();
        response.setToken("dummy.jwt.token-" + user.getUsername()); // TODO: Implement proper JWT token generation
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
        logger.debug("Registration request for user: {}", registerRequest.getUsername());
        User user = userService.createUser(registerRequest.getUsername(), registerRequest.getPassword());
        
        AuthResponse response = new AuthResponse();
        response.setToken("dummy.jwt.token-" + user.getUsername()); // TODO: Implement proper JWT token generation
        return ResponseEntity.status(201).body(response);
    }
}