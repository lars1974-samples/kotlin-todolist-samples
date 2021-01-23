package dk.lrn.wishlist.springboot

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
class TodoList (
    @Id
    @GeneratedValue
    var id: Long? = null,
    var name: String?,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var itemTodos: MutableList<TodoListItem>
)

@Repository
interface TodoListRepository : CrudRepository<TodoList, Long> {


}


