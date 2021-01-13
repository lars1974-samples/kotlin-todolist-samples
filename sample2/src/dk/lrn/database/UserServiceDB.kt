package dk.lrn.database

import com.google.gson.annotations.Expose
import dk.lrn.CreateMyAppUserCommand
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

class UserService(private val dbc: DatastoreConnection)  {
    suspend fun list(): List<UserDTO> {
        return dbc.query {
            UserEntity.all().map { it.toModel() }
        }
    }

    suspend fun create(newUserCmd: CreateMyAppUserCommand): UserDTO {
        return dbc.query {
            UserEntity.new {
                this.email = newUserCmd.email
                this.realName = newUserCmd.realName
            }.toModel()
        }
    }
}

data class UserDTO(@Expose val email: String, @Expose val realName: String, @Expose val items : List<ToDoItem>)


object UserTable : LongIdTable("my_app_user_table") {
    val email = varchar("user_email", 255).uniqueIndex()
    val realName = varchar("real_name", 255)

}

class UserEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserEntity>(UserTable)

    var email by UserTable.email
    var realName by UserTable.realName
    val items by ToDoItemDAO referrersOn ToDoItemTable.user
    fun toModel(): UserDTO {
        return UserDTO(email, realName, items.map { it.toModel()})
    }
}