package dk.lrn.database

import org.jetbrains.exposed.dao.id.LongIdTable


internal object Users : LongIdTable() {
        val name = varchar("name", 50)
        val email = varchar("email", 50)
        val todoLists = reference("userId", TodoLists)
    }


