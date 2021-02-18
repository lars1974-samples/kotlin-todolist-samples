package dk.lrn.wishlist.springboot.database

import dk.lrn.wishlist.springboot.TodoItemEntity
import javax.persistence.*

@Entity
class TodoListEntity (
    val userId: Long,
    var name: String,

    @OneToMany(cascade = [CascadeType.ALL])
    var itemTodoEntities: MutableList<TodoItemEntity>
    )
{
    @Id @GeneratedValue var id: Long = 0
}




