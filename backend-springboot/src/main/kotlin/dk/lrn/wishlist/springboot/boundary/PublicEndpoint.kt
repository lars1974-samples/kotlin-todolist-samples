package dk.lrn.wishlist.springboot.boundary

import dk.lrn.wishlist.springboot.UserService
import dk.lrn.wishlist.springboot.contract.PublicEndpointInterface
import org.springframework.web.bind.annotation.RestController

@RestController
class PublicEndpoint(
    val userService: UserService
) : PublicEndpointInterface{
    override fun whoAmI(): String {
        return ""+userService.getIdentificationDetail()
    }
}