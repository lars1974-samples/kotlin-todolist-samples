package dk.lrn

import com.google.gson.annotations.Expose
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.gson.*
import java.text.*


fun main() {
    //Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
    val con = Database.connect(hikari())

    initDatabase()

    embeddedServer(Netty, port = 8000) {
        wishModule()
        webModule()
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



