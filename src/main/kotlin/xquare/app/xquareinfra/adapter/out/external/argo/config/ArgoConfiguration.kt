package xquare.app.xquareinfra.adapter.out.external.argo.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ArgoConfiguration {
    
    @Bean
    @ConfigurationProperties(prefix = "argo")
    fun argoProperties(): ArgoProperties {
        return ArgoProperties()
    }
}

class ArgoProperties {
    var server = Server()
    var namespace: String = "argo"
    var auth = Auth()
    
    class Server {
        var url: String = "http://localhost:2746"
    }
    
    class Auth {
        var token: String = ""
        var username: String = ""
        var password: String = ""
    }
}
