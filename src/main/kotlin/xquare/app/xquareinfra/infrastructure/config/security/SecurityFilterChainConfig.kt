package xquare.app.xquareinfra.infrastructure.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import xquare.app.xquareinfra.infrastructure.error.filter.ErrorLogResponseFilter
import xquare.app.xquareinfra.infrastructure.error.filter.ExceptionConvertFilter
import xquare.app.xquareinfra.infrastructure.security.jwt.JwtFilter
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.SecurityContextHolderFilter
import org.springframework.stereotype.Component

@Component
class SecurityFilterChainConfig(
    private val jwtFilter: JwtFilter,
    private val objectMapper: ObjectMapper
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(builder: HttpSecurity) {
        builder.run {
            addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            addFilterBefore(ExceptionConvertFilter(), SecurityContextHolderFilter::class.java)
            addFilterBefore(ErrorLogResponseFilter(objectMapper), ExceptionConvertFilter::class.java)
        }
    }
}
