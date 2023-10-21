package dk.lrn.wishlist.springboot.database

import javax.persistence.*

@Entity
data class TodoItemEntity(
    var listId: Long,
    val userId: Long,
    var description: String,
    var done: Boolean,

){
    @Id @GeneratedValue var id: Long = 0
}


