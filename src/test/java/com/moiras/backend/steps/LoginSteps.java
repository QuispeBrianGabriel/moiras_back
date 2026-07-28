package com.moiras.backend.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.spring.CucumberContextConfiguration;
import java.util.Map;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginSteps {

  @LocalServerPort private int port;

  private ResponseEntity<Map> responseEntity;

  @Dado("que el usuario {string} existe con contraseña {string}")
  public void que_el_usuario_existe_con_contrasena(String username, String password) {
    // Preparación futura con base de datos
  }

  @Cuando("ingreso las siguientes credenciales:")
  public void ingreso_las_siguientes_credenciales(io.cucumber.datatable.DataTable dataTable) {
    Map<String, String> credentials = dataTable.asMaps().get(0);

    RestClient restClient = RestClient.create("http://localhost:" + port);

    responseEntity =
        restClient
            .post()
            .uri("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(credentials)
            .retrieve()
            .toEntity(Map.class);
  }

  @Entonces("ingreso correctamente al sistema")
  public void ingreso_correctamente_al_sistema() {
    assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();

    Map<String, Object> body = responseEntity.getBody();
    assertThat(body).isNotNull();
    assertThat(body.get("token")).isNotNull();
  }
}
