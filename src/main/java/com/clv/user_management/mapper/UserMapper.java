package com.clv.user_management.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.clv.user_management.model.User;

@Mapper
public interface UserMapper {

    List<User> findAll();

    User findById(Long id);

    int insert(User user);

    int update(User user);

    int softDelete(@Param("id") Long id, @Param("updateBy") String updateBy,
            @Param("updateTime") java.time.LocalDateTime updateTime);
}
