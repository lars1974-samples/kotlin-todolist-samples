package dk.lrn

import com.google.gson.annotations.Expose
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
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
import java.time.*


fun main() {
    //Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
    Database.connect(hikari())


    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(Users)

        User.new {
            name = "Jakob"
            age = 25
        }

        User.new {
            name = "Lars"
            age = 24
        }

        //println(User.all().first())
    }

    embeddedServer(Netty, port = 8000) {


        install(ContentNegotiation) {
            gson {
                //enable(SerializationFeature.INDENT_OUTPUT)
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
                excludeFieldsWithoutExposeAnnotation()
            }
        }
        routing {
            get ("/") {
                    //convert to DTOs
                    val u = getUsers().map { UserDto(it.id.value, it.name, it.age) }
                    call.respond(HttpStatusCode.OK, u)
            }
        }
    }.start(wait = true)
}


fun getUsers(): List<User> {
    var users: List<User>? = null

    transaction {
        users = User.all().toList();
    }
    return users!!
}

object Users : IntIdTable() {
    val name = varchar("name", 50).index()
    val age = integer("age")
}

class User(id: EntityID<Int>) : IntEntity(id) {
    constructor(id: EntityID<Int>, name: String): this(id){
        this.name = name
    }


    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var age by Users.age


    override fun toString(): String {
        return "name $name age: $age"
    }
}

data class UserDto(
    @Expose val id: Int, @Expose val name : String, @Expose val age:Int)


private fun hikari(): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.h2.Driver"
    config.jdbcUrl = "jdbc:h2:mem:test"
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}






