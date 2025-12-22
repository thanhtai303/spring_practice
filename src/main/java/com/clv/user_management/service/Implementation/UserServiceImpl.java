package com.clv.user_management.service.Implementation;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.clv.user_management.model.User;
import com.clv.user_management.mapper.UserMapper;
import com.clv.user_management.dto.request.CreateUserRequest;
import com.clv.user_management.dto.request.UpdateUserRequest;
import com.clv.user_management.dto.response.ResponseObject;
import com.clv.user_management.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ResponseObject<List<User>> getAllUsers() {
        List<User> users = userMapper.findAll();
        return new ResponseObject<>("SUCCESS", "Get all users successfully", users);
    }

    @Override
    public ResponseObject<User> getUserById(Long id) {
        User user = userMapper.findById(id);
        return new ResponseObject<>("SUCCESS", "Get user successfully", user);
    }

    @Override
    public ResponseObject<Void> createUser(CreateUserRequest createUserRequest, String currentUser) {
        var now = LocalDateTime.now();
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        user.setStatus(createUserRequest.getStatus());
        user.setCreateBy(currentUser);
        user.setCreateTime(now);
        user.setUpdateBy(currentUser);
        user.setUpdateTime(now);

        userMapper.insert(user);
        return new ResponseObject<>("SUCCESS", "User created successfully", null);
    }

    @Override
    public ResponseObject<Void> updateUser(Long id, UpdateUserRequest request, String currentUser) {
        User user = userMapper.findById(id);
        if (user == null) {
            return new ResponseObject<>("FAIL", "User not found", null);
        }

        if (request.getUsername() != null)
            user.setUsername(request.getUsername());
        if (request.getEmail() != null)
            user.setEmail(request.getEmail());
        if (request.getPassword() != null)
            user.setPassword(request.getPassword());
        if (request.getStatus() != null)
            user.setStatus(request.getStatus());

        var now = LocalDateTime.now();
        user.setUpdateBy(currentUser);
        user.setUpdateTime(now);

        userMapper.update(user);
        return new ResponseObject<>("SUCCESS", "User updated successfully", null);
    }

    @Override
    public ResponseObject<Void> deleteUser(Long id, String currentUser) {
        userMapper.softDelete(id, currentUser, LocalDateTime.now());
        return new ResponseObject<>("SUCCESS", "User deleted successfully", null);
    }
}
