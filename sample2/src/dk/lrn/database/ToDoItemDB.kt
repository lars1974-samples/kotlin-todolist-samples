package dk.lrn.database

import com.google.gson.annotations.Expose
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

class ToDoItemDB {


}

object ToDoItemTable : LongIdTable("my_to_do_item_table") {
    val item = varchar("item", 255)
    val priority = integer("priority")
    val user = reference("user", UserTable)
}

data class ToDoItem(@Expose val item: String, @Expose val priority: Int)


class ToDoItemDAO(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ToDoItemDAO>(ToDoItemTable)

    var item by ToDoItemTable.item
    var priority by ToDoItemTable.priority
    var user by UserEntity referencedOn ToDoItemTable.user
    fun toModel(): ToDoItem {
        return ToDoItem(item, priority)
    }
}
