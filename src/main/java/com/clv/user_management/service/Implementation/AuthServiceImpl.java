package com.clv.user_management.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.clv.user_management.config.JwtService;
import com.clv.user_management.dto.request.CreateUserRequest;
import com.clv.user_management.dto.request.LoginRequest;
import com.clv.user_management.dto.request.RegisterRequest;
import com.clv.user_management.dto.response.LoginResponse;
import com.clv.user_management.dto.response.ResponseObject;
import com.clv.user_management.service.AuthService;
import com.clv.user_management.service.UserService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService usrService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public ResponseObject<Void> register(RegisterRequest request) {
        if (usrService.getUserByEmail(request.getEmail()).getData() != null) {
            throw new RuntimeException("Email already exists");
        }

        CreateUserRequest usr = new CreateUserRequest();
        usr.setEmail(request.getEmail());
        usr.setUsername(request.getUsername());
        usr.setPassword(passwordEncoder.encode(request.getPassword()));
        usr.setStatus("ACTIVE");

        usrService.createUser(usr, "SYSTEM");
        return new ResponseObject<>("SUCCESS", "Register success", null);
    }

    @Override
    public ResponseObject<LoginResponse> login(LoginRequest request) {
        String token = jwtService.loginHandle(request.getEmail(), request.getPassword());
        if (token == null) {
            throw new RuntimeException("Invalid email or password");
        }
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return new ResponseObject<>("SUCCESS", "Login success", response);
    }

}
