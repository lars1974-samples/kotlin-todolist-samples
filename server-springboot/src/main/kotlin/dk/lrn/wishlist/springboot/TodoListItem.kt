package dk.lrn.wishlist.springboot

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*


@Entity
data class TodoListItem(
    @Id @GeneratedValue var id: Long? = null,
    var description: String?,
    var done: Boolean
)

@Repository
interface ListItemRepository : CrudRepository<TodoListItem, Long> {


}
