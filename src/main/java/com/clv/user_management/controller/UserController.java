package com.clv.user_management.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clv.user_management.dto.request.CreateUserRequest;
import com.clv.user_management.dto.request.UpdateUserRequest;
import com.clv.user_management.dto.response.ResponseObject;
import com.clv.user_management.model.User;
import com.clv.user_management.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResponseObject<List<User>>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<User>> getById(@PathVariable Long id) {
        return ResponseEntity
                .ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseObject<Void>> create(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.createUser(createUserRequest, "admin"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> update(@PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity
                .ok(userService.updateUser(id, updateUserRequest, "admin"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> delete(@PathVariable Long id) {
        return ResponseEntity
                .ok(userService.deleteUser(id, "admin"));
    }
}
