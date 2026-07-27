# language: es

Característica: Inicio de Sesion
  # Como usuario registrado
  # Quiero iniciar sesion
  # Para acceder al sistema

  Escenario: Inicio de sesion exitoso con credenciales validas
    Dado que el usuario "testuser" existe con contraseña "Secret123!"
    Cuando ingreso las siguientes credenciales:
      | username | password   |
      | testuser | Secret123! |
    Entonces ingreso correctamente al sistema
