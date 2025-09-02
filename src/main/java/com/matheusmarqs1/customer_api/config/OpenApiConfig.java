package com.matheusmarqs1.customer_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Profile({"dev", "test"})
@Configuration
@OpenAPIDefinition(
  info =@Info(
    title = "Customer API",
    version = "1.0",
    contact = @Contact(
      name = "Matheus Teles", email = "matheusti70@gmail.com", url = "https://github.com/matheusmarqs1"
    ),
    license = @License(
      name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
    )
  ),
  servers = @Server(
    url = "http://localhost:8080",
    description = "Development Server"
  )
)

@SecurityScheme(
  name = "Bearer Authentication",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)
public class OpenApiConfig {

}
