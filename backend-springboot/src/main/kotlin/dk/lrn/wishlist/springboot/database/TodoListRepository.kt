package dk.lrn.wishlist.springboot.database

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoListRepository : CrudRepository<TodoListEntity, Long> {
    fun findByUserId(userId: Long): List<TodoListEntity>
    fun getByUserIdAndId(userId: Long, id: Long): TodoListEntity

}