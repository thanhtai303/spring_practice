package com.clv.user_management.service;

import com.clv.user_management.dto.request.LoginRequest;
import com.clv.user_management.dto.request.RegisterRequest;
import com.clv.user_management.dto.response.LoginResponse;
import com.clv.user_management.dto.response.ResponseObject;

public interface AuthService {
    ResponseObject<Void> register(RegisterRequest request);

    ResponseObject<LoginResponse> login(LoginRequest request);

}
