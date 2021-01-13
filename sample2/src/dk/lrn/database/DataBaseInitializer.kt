package dk.lrn.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

object DataBaseInitializer {
    fun initDatabase() {
        Database.connect(DatastoreConnection.quickNDirtyDb())
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(UserTable)
            SchemaUtils.create(ToDoItemTable)

            val id1 = UserTable.insert {
                it[email] = "J@kob"
                it[realName] = "jakob"
            } get UserTable.id

            val id2 = ToDoItemTable.insert {
                it[item] = "Købe ind"
                it[priority] = 1
                it[user] = id1
            } get ToDoItemTable.id

            val id3 = ToDoItemTable.insert {
                it[item] = "Købe ind igen"
                it[priority] = 2
                it[user] = id1
            } get ToDoItemTable.id

            val dao = UserEntity.new {
                email = "jakob@lb.dk"
                realName = "peter jensen"

//            items = SizedCollection(listOf<ToDoItemDAO>(ToDoItemDAO.new {
//                    item="indkøb"
//                    priority=1
//                }))
            }

            val item1 = ToDoItemDAO.new {
                item="indkøb"
                priority=1
                user = dao
            }

            val item2 = ToDoItemDAO.new {
                item="indkøb2"
                priority=12
                user = dao
            }
        }
    }
}