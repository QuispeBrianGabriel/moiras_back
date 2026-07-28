package com.moiras.backend.domain.services;

import com.moiras.backend.services.LoginService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

  @Override
  public boolean authenticate(String username, String password) {
    return "testuser".equals(username) && "Secret123!".equals(password);
  }
}
