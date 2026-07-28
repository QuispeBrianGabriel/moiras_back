package com.moiras.backend.services;

public interface LoginService {
    boolean authenticate(String username, String password);
}
