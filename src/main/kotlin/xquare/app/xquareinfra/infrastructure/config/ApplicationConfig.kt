package xquare.app.xquareinfra.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = ["xquare.app.xquareinfra"]
)
@ConfigurationPropertiesScan(basePackages = ["xquare.app.xquareinfra"])
class ApplicationConfig