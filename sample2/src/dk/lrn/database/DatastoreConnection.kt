package dk.lrn.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

class DatastoreConnection() {
    companion object {
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
    }
    private val database: Database by lazy {
        Database.connect(quickNDirtyDb())
    }

    suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) {
        transaction(database) {
             block()
        }
    }


}