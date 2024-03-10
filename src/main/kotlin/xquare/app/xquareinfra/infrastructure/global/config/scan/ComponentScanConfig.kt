package xquare.app.xquareinfra.infrastructure.global.config.scan

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = ["xquare.app.xquareinfra"]
)
class ComponentScanConfig
