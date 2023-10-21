package dk.lrn.wishlist.springboot

import dk.lrn.wishlist.springboot.boundary.ErrorMessage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.jwt.Jwt

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.core.convert.converter.Converter
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.http.HttpStatus

import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.security.config.annotation.web.builders.WebSecurity
import java.lang.Exception


@SpringBootApplication
class WishlistSpringbootApplication

fun main(args: Array<String>) {
    runApplication<WishlistSpringbootApplication>(*args)
}

@Suppress("unused")
@OpenAPIDefinition(info = Info(title = "My API", version = "v1"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
class OpenApi30Config



@EnableWebSecurity
class BasicConfiguration : WebSecurityConfigurerAdapter() {
    @Value("\${admins}")
    lateinit var admins: List<String>

    override fun configure(http: HttpSecurity) {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter(JWTConverter(admins))

        http.cors()
            .and()
            .authorizeRequests()
            .antMatchers("/", "/whoami", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
            .antMatchers("/admin/**").hasRole("admins")
            .anyRequest().hasRole("users")
            .and()
            .oauth2ResourceServer().jwt().jwtAuthenticationConverter(converter)
    }
}

class JWTConverter(val admins: List<String>) : Converter<Jwt, Collection<GrantedAuthority>> {
    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {

        val access = jwt.claims["realm_access"] as Map<*, *>
        val roles = access["roles"] as List<*>
        val list = roles.map { SimpleGrantedAuthority("ROLE_$it") }.toMutableList()
        if (admins.contains(jwt.claims["email"])) list.add(SimpleGrantedAuthority("ROLE_admins"))
        list.add(SimpleGrantedAuthority("ROLE_users"))
        return list
    }
}

@ControllerAdvice
class GeneralExceptionHandler {
    @ExceptionHandler(EmptyResultDataAccessException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected fun handleException(ex: EmptyResultDataAccessException): ResponseEntity<ErrorMessage> {
        return ResponseEntity<ErrorMessage>(ErrorMessage("element not found in database"),HttpStatus.valueOf(404))
    }
}
