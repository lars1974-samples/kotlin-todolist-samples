package dk.lrn.wishlist.springboot.database

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoItemRepository : CrudRepository<TodoItemEntity, Long> {
    fun getByUserIdAndId(userId: Long, id: Long): TodoItemEntity
    fun findByUserId(userId: Long): List<TodoItemEntity>
}