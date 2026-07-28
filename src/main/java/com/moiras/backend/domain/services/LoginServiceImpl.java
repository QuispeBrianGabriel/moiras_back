package com.moiras.backend.domain.services;

import org.springframework.stereotype.Service;
import com.moiras.backend.services.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public boolean authenticate(String username, String password) {
        return "testuser".equals(username) && "Secret123!".equals(password);
    }
}
