package dk.lrn.wishlist.springboot.database

import dk.lrn.wishlist.springboot.TodoItemEntity
import javax.persistence.*

@Entity
class TodoListEntity (
    val userId: Long,
    var name: String
    )
{
    @Id @GeneratedValue var id: Long = 0

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var itemTodoEntities: MutableList<TodoItemEntity> = mutableListOf()
}




