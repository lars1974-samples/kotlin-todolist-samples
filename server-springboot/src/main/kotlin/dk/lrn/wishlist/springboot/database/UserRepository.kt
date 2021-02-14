package dk.lrn.wishlist.springboot.database

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<UserEntity, Long> {
    fun findUserByEmail(user: String): UserEntity?
}