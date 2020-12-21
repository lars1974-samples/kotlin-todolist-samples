package dk.lrn

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.larsmodule(testing: Boolean = false) {


    routing() {
        route("json") {
            get() {
                call.respond(HttpStatusCode.OK, mapOf(Pair("test", "value")))
            }
        }
    }
}