package dk.lrn

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction


fun hikari(): HikariDataSource {
    val config = HikariConfig()
    config.driverClassName = "org.h2.Driver"
    config.jdbcUrl = "jdbc:h2:mem:test"
    config.maximumPoolSize = 3
    config.isAutoCommit = false
    config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    config.validate()
    return HikariDataSource(config)
}

fun initDatabase() {
    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(Users)
        SchemaUtils.create(WishItems)

        val id1 = Users.insert {
            it[name] = "Jakob"
            it[age] = 25
        } get Users.id

        val id2 = Users.insert {
            it[name] = "Lars"
            it[age] = 24
        } get Users.id


        WishItems.insert {
            it[name] = "Tesla model S"
            it[price] = 500000
            it[url] = "www.tesla.com"
            it[shop] = "Tesla, Hillerød"
            it[WishItems.userId] = id1.value
        }

        WishItems.insert {
            it[name] = "Ford Focus"
            it[price] = 5000
            it[url] = "www.ford.com"
            it[shop] = "Ford inc"
            it[WishItems.userId] = id1.value
        }

        WishItems.insert {
            it[name] = "Ford Fiesta"
            it[price] = 1000
            it[url] = "www.ford.com"
            it[shop] = "Ford inc"
            it[WishItems.userId] = id2.value
        }

        WishItems.insert {
            it[name] = "Hoverboard"
            it[price] = 5000
            it[url] = "www.hover.com"
            it[shop] = "Hover inc"
            it[WishItems.userId] = id2.value
        }
    }
}
