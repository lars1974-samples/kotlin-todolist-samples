package dk.laj.ktor.exposed.dao

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*

import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.respond

fun main(args: Array<String>) {

    io.ktor.server.netty.EngineMain.main(args)

}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    org.jetbrains.exposed.sql.Database.connect("jdbc:h2:~/lrntestdb", driver = "org.h2.Driver")

    CitiesDAO().initDatabase()
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }

        post("/cities"){
            val message = call.receive<CityDto>()


            call.respond(CitiesDAO().add(message))

        }

        get("/cities"){
            call.respond(CitiesDAO().all())

        }
    }
}

