package dk.lrn.wishlist.springboot

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
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


