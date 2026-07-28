package com.moiras.backend.steps;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;

public class LoginSteps {

  @Dado("que el usuario {string} existe con contraseña {string}")
  public void que_el_usuario_existe_con_contrasena(String username, String password) {
    throw new io.cucumber.java.PendingException();
  }

  @Cuando("ingreso las siguientes credenciales:")
  public void ingreso_las_siguientes_credenciales(io.cucumber.datatable.DataTable dataTable) {
    throw new io.cucumber.java.PendingException();
  }

  @Entonces("ingreso correctamente al sistema")
  public void ingreso_correctamente_al_sistema() {
    throw new io.cucumber.java.PendingException();
  }
}
