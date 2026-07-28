package com.moiras.backend.controllers;

import com.moiras.backend.services.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final LoginService loginService;

  public AuthController(LoginService loginService) {
    this.loginService = loginService;
  }

  public record LoginRequest(String username, String password) {}

  public record LoginResponse(String message, String token) {}

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    boolean isAuthenticated = loginService.authenticate(request.username(), request.password());

    if (isAuthenticated) {
      return ResponseEntity.ok(new LoginResponse("Login exitoso", "fake-jwt-token-12345"));
    }

    return ResponseEntity.status(401).body(new LoginResponse("Credenciales inválidas", null));
  }
}
