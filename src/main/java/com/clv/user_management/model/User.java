package com.clv.user_management.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {

    private Long id;
    private String username;
    private String email;
    private String password;
    private String status;

    private String deleteFlag;

    private String createBy;
    private LocalDateTime createTime;

    private String updateBy;
    private LocalDateTime updateTime;
}
