package xquare.app.xquareinfra.infrastructure.config.security

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
                .antMatchers("/v1/team").permitAll()
                .antMatchers("/v1/auth/**").permitAll()
                .antMatchers("/v1/deploy/**/approve").permitAll()
                .antMatchers(("/v1/container/sync")).permitAll()
                .antMatchers("/v1/logs").permitAll()
                .antMatchers("/v2/container/**/**/sync-domain").permitAll()
                .antMatchers("/v1/github/**").permitAll()
            anyRequest().authenticated()
        }
    }
}
