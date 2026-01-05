package com.clv.user_management.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.clv.user_management.mapper.UserMapper;
import com.clv.user_management.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper usrMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usr = usrMapper.findByEmail(username);
        if (usr == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                usr.getId().toString(),
                usr.getPassword(),
                Collections.emptyList());
    }
}
