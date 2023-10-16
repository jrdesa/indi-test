package com.in.jrfc.conf;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Juan Ramón FC",
                        email = "email@gmail.com"),
                title = "Price Service API",
                version = "2.0",
                description = "Test Java Developer - Inditex",
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"


                ),
                termsOfService = "http://swagger.io/terms/"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8081"
                ),
                @Server(
                        description = "Prod ENV",
                        url = "https://prices-now-service.***prod*url.com/price"
                )

        })
     @SecurityScheme(

                name = "basicAuth",
                description = "Basic Authentication, without credentials, just click on Authorize button.",
                type = SecuritySchemeType.HTTP,
                scheme = "basic"
        )

@Configuration
public class OpenApiConfig {

}
