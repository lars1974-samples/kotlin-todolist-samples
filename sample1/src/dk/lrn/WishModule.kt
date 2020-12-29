package dk.lrn

import com.google.gson.annotations.Expose
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.wishModule(){
    routing {
        route("/api") {
            get() {
                //convert to DTOs
                val u0 = getUsers();
                val u = u0.map {
                    val wi = getWishItemsByUser(it)
                    //val widtos = wi.map { WishItemDto(it.id.value, it.name, it.price, it.url, it.shop) }.toList()
                    val widtos = wi.map { WishItemDto(1, it.name, it.price, it.url, it.shop) }.toList()
                    //val widtos = SizedCollection(listOf<WishItemDto>(WishItemDto(1, "p", 100, "f", "f")))
                    val udto = UserDto(it.id.value, it.name, it.age, widtos)
                    udto
                }.toList()
                call.respond(HttpStatusCode.OK, u)
            }
        }
        route("/api2") {
            get() {
                val u = UserDao().list()
                println(u)
                call.respond(HttpStatusCode.OK, u)
            }
        }
    }
}


fun getUserDtoWithWishItemDtos(id:Int):UserDto? {
    val user = getUser(id);
    if (user==null){
        return null
    }
    val wishItemsByUser = getWishItemsByUser(user);
    val wiList = wishItemsByUser.mapNotNull { toWishItemDto(it) }.toList()
    return toUserDto(user, wiList)
}

fun toWishItemDto(wi:WishItem):WishItemDto =
    WishItemDto(id=wi.id.value,name = wi.name, price = wi.price, url = wi.url, shop = wi.shop)

fun toUserDto(user:User, wiList:List<WishItemDto>):UserDto =
    UserDto(id=user.id.value,name = user.name, age = user.age, items = wiList)


fun getUsers(): List<User> {
    var users: List<User>? = null

    transaction {
        users = User.all().toList();
    }
    return users!!
}

fun getUser(id:Int): User? {
    var user: User? = null

    transaction {
        user = User.findById(id)
    }
    return user
}

fun getWishItemsByUser(user:User): List<WishItem> {

    var items: List<WishItem>? = null

    transaction {
        val query = WishItems.select{WishItems.userId eq user.id.value}
        items = query.mapNotNull { toWishItem(it) }
    }
    return items!!
}

private  fun toWishItem(row: ResultRow): WishItem  {
    val wi = WishItem(id = row[WishItems.id], name = row[WishItems.name], price = row[WishItems.price], url = row[WishItems.url], shop = row[WishItems.shop])
    return wi
}


object WishItems : IntIdTable() {
    val name = varchar("item", 50).index()
    val price = integer("price")
    val url = varchar("url", 80)
    val shop = varchar("shop", 30)
    val userId = integer("user_id")
        .references(Users.id)
}

class WishItem(id: EntityID<Int>, @Expose var name:String, var price:Int, var url:String, var shop:String) : IntEntity(id) {
    companion object : IntEntityClass<WishItem>(WishItems)
    override fun toString(): String {
        return "WishItem(name='$name', price=$price, url='$url', shop='$shop')"
    }
    fun toModel() : WishItemDto {
        return WishItemDto(id.value, name, price, url, shop)
    }
}


data class WishItemDto(
    @Expose val id: Int, @Expose val name: String, @Expose val price:Int, @Expose val url:String, @Expose val shop:String)
