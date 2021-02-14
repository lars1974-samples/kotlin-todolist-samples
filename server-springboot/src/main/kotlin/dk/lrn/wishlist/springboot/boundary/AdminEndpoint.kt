package dk.lrn.wishlist.springboot.boundary

import dk.lrn.wishlist.springboot.contract.AdminEndpointInterface
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminEndpoint: AdminEndpointInterface {
    override fun congrats(): String {
        return "congrates you are admin"
    }
}