package dk.lrn

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.freemarker.respondTemplate
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.webModule() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    routing {
        route("/") {
            get() {
                        call.respond(FreeMarkerContent("index.ftl",null))
            }
        }

        route("/web") {
            get("/wishlist/{user}") {
                //val wl = listOf<WishItemDto>(WishItemDto(1, "it.name", 100, "it.url", "it.shop"))
                //val sampleUser = UserDto(1, "Jakob", 10,  wl)
                val id = call.parameters["user"]
                if (id == null) {
                    call.respond(status = HttpStatusCode.NotFound, "No id")
                } else {
                    val userDto = getUserDtoWithWishItemDtos(id.toInt())
                    if (userDto != null) {
                        call.respond(FreeMarkerContent("wishlist.ftl", mapOf("user" to userDto)))
                    } else {
                        call.respond(status = HttpStatusCode.NotFound, "No user with id $id")
                    }
                }
            }
        }
    }
}


