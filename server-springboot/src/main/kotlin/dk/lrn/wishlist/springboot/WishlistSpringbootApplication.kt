package dk.lrn.wishlist.springboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.jwt.Jwt

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.core.convert.converter.Converter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter

@SpringBootApplication
class WishlistSpringbootApplication

fun main(args: Array<String>) {
    runApplication<WishlistSpringbootApplication>(*args)
}

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
    override fun convert(jwt: Jwt): Collection<GrantedAuthority>? {
        val access = jwt.claims["realm_access"] as Map<*, *>
        val roles = access["roles"] as List<*>
        val list = roles.map { SimpleGrantedAuthority("ROLE_$it") }.toMutableList()
        if (admins.contains(jwt.claims["email"])) list.add(SimpleGrantedAuthority("ROLE_admins"))
        list.add(SimpleGrantedAuthority("ROLE_users"))
        return list
    }
}
