package ae.exafy.taskmanager.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(type = SecuritySchemeType.HTTP,
        name = "basicAuth",
        scheme = "basic")
@Configuration
public class OpenApiConfig {
}
