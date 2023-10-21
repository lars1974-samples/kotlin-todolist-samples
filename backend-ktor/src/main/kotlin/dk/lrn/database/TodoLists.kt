package dk.lrn.database

import org.jetbrains.exposed.dao.id.LongIdTable

internal object TodoLists : LongIdTable() {
    val userId = long("userId")
    val name = varchar("name",50)
    val todoItems = reference("id", TodoItems)
}

