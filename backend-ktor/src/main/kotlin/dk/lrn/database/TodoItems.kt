package dk.lrn.database

import org.jetbrains.exposed.dao.id.LongIdTable


internal object TodoItems : LongIdTable() {
    val userId = long("userId")
    val name = varchar("name",50)
    var description = varchar("description",200)
    var done = bool("done")
}
