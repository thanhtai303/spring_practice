package com.clv.user_management.service;

import java.util.List;
import com.clv.user_management.model.User;
import com.clv.user_management.dto.request.CreateUserRequest;
import com.clv.user_management.dto.request.UpdateUserRequest;
import com.clv.user_management.dto.response.ResponseObject;

public interface UserService {

    ResponseObject<List<User>> getAllUsers();

    ResponseObject<User> getUserById(Long id);

    ResponseObject<User> getUserByEmail(String email);

    ResponseObject<Void> createUser(CreateUserRequest createUserRequest, String currentUser);

    ResponseObject<Void> updateUser(Long id, UpdateUserRequest request, String currentUser);

    ResponseObject<Void> deleteUser(Long id, String currentUser);
}
