package dk.lrn.wishlist.springboot.database

import dk.lrn.wishlist.springboot.TodoItemEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoItemRepository : CrudRepository<TodoItemEntity, Long> {
    fun getByUserIdAndId(userId: Long, id: Long): TodoItemEntity
}