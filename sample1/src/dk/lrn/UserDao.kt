package dk.lrn

import com.google.gson.annotations.Expose
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

class UserDao() {
    suspend fun list() : List<UserDto> {
        var v : List<UserDto> = listOf()
        transaction {
            val v2 =  User.all().with(User::wishItems).toList() //.map{it.toModel()}
            println(v2)

        }
        return v
    }
}

internal object Users : IntIdTable() {
    val name = varchar("name", 50).index()
    val age = integer("age")
}


data class UserDto(
    @Expose val id: Int, @Expose val name : String, @Expose val age:Int, @Expose val items : List<WishItemDto>)


class User(id: EntityID<Int>) : IntEntity(id) {
    constructor(id: EntityID<Int>, name: String): this(id){
        this.name = name
    }
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var age by Users.age
    val wishItems by WishItem referrersOn WishItems.userId

    override fun toString(): String {
        return "name $name age: $age"
    }

    fun toModel(): UserDto {
        val wi = listOf<WishItemDto>(WishItemDto(1,"hhh", 100, "ddd", "sss"))
        return UserDto(1, "nam", 1, wi)
    }
}