package org.example

import com.google.gson.annotations.Expose
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.MyAppUserTable.email
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.text.DateFormat
import javax.sql.DataSource

fun main() {
    val con = Database.connect(quickNDirtyDb())
    initDatabase()
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

fun initDatabase() {
    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(MyAppUserTable)

        val id1 = MyAppUserTable.insert {
            it[email] = "J@kob"
            it[realName] = "jakob"
        } get MyAppUserTable.id

    }
}

// Data model - data classes for input and output formats
data class MyAppUser(@Expose val email: String, @Expose val realName: String)

data class CreateMyAppUserCommand(@Expose val email: String, @Expose val realName: String)

// Core service definition
internal interface MyAppUserService {
    suspend fun list(): List<MyAppUser>
    suspend fun create(newUserCmd: CreateMyAppUserCommand): MyAppUser
}

// Data store connection setup shennanigans
fun quickNDirtyDb(): DataSource {
    return HikariDataSource(HikariConfig().apply {
        poolName = "HIKARI-POOL"
        driverClassName = "org.h2.Driver"
        jdbcUrl = "jdbc:h2:mem:test"
        maximumPoolSize = 5
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_READ_COMMITTED"
        validate()
    })
}

class DatastoreConnection(private val dataSource: DataSource) {
    private val database: Database by lazy {
        Database.connect(dataSource)
    }

    suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction(database) {
            block()
        }
    }
}

// Object relational mapping
internal object MyAppUserTable : LongIdTable("my_app_user_table") {
    val email = varchar("user_email", 255).uniqueIndex()
    val realName = varchar("real_name", 255)
}

internal class MyAppUserDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MyAppUserDAO>(MyAppUserTable)

    var email by MyAppUserTable.email
    var realName by MyAppUserTable.realName

    fun toModel(): MyAppUser {
        return MyAppUser(email, realName)
    }
}

// Service implementation for datastore of choice
internal class MyAppUserServiceImpl(private val dbc: DatastoreConnection) : MyAppUserService {
    override suspend fun list(): List<MyAppUser> {
        return dbc.query {
            MyAppUserDAO.all().map { it.toModel() }
        }
    }

    override suspend fun create(newUserCmd: CreateMyAppUserCommand): MyAppUser {
        return dbc.query {
            MyAppUserDAO.new {
                this.email = newUserCmd.email
                this.realName = newUserCmd.realName
            }.toModel()
        }
    }
}

//REST-ish API
fun Application.apiModule(){
    routing {
        route("/api") {

            val service: MyAppUserService = MyAppUserServiceImpl(DatastoreConnection(quickNDirtyDb()))

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