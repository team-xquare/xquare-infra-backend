package xquare.app.xquareinfra.infrastructure.global.config.scan

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@ConfigurationPropertiesScan(basePackages = ["xquare.app.xquareinfra"])
@Configuration
class PropertiesScanConfig
