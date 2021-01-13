package dk.lrn

import com.google.gson.annotations.Expose
import dk.lrn.database.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.text.DateFormat

fun main() {
    DataBaseInitializer.initDatabase()
    embeddedServer(Netty, port = 8000) {
            apiModule()
            install(ContentNegotiation) {
            gson {
                //enable(SerializationFeature.INDENT_OUTPUT)
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
                excludeFieldsWithoutExposeAnnotation()
            }
        }
    }.start(wait = true)
}

data class CreateMyAppUserCommand(@Expose val email: String, @Expose val realName: String)

//REST-ish API
fun Application.apiModule(){
    routing {
        route("/api") {

            val service = UserService(DatastoreConnection())

            get("/myUsers") {
                call.respond(HttpStatusCode.OK, service.list())
            }

            post("/myUsers") {
                //Don't forget to validate the input...
                val newUser = service.create(call.receive())
                call.respond(HttpStatusCode.OK, newUser)
            }
        }
    }
}