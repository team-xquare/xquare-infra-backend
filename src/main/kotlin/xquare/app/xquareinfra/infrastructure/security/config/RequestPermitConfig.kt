package xquare.app.xquareinfra.infrastructure.security.config

import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsUtils

@Component
class RequestPermitConfig : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(builder: HttpSecurity) {
        builder.authorizeRequests().run {
            requestMatchers(CorsUtils::isCorsRequest).permitAll()
                .antMatchers("/team").permitAll()
            anyRequest().authenticated()
        }
    }
}
