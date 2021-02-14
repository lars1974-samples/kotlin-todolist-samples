package dk.lrn.wishlist.springboot.contract

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@SecurityRequirement(name = "bearerAuth")
@RequestMapping("admin")
interface AdminEndpointInterface {
    @GetMapping("/test")
    fun congrats(): String
}