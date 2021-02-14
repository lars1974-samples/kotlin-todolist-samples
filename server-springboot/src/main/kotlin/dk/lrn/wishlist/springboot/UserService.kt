package dk.lrn.wishlist.springboot

import dk.lrn.wishlist.springboot.database.TodoListEntity
import dk.lrn.wishlist.springboot.database.TodoListRepository
import dk.lrn.wishlist.springboot.database.UserEntity
import dk.lrn.wishlist.springboot.database.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class UserService(
    val userRepository: UserRepository,
    val todoListRepository: TodoListRepository
) {
    @Value("\${admins}")
    lateinit var admins: List<String>

    fun getUserId(): Long {
        val jwt = SecurityContextHolder.getContext().authentication.credentials as Jwt
        val email = jwt.claims["email"] as String
        val user = userRepository.findUserByEmail(email)
        return user?.id ?: createUser(email).id!!

    }

    fun createUser(email: String): UserEntity {
        userRepository.save(UserEntity(email,"name"))
        val newUser = userRepository.findUserByEmail(email)!!
        val todoList = TodoListEntity(newUser.id!!,"My First TodoList", mutableListOf(TodoItemEntity(newUser.id!!,"first item", false)))
        todoListRepository.save(todoList)
        return newUser
    }

    fun getIdentificationDetail(): String{
        val token = SecurityContextHolder.getContext().authentication.credentials
        return if(token is Jwt) {
            val sb = StringBuilder()
            token.claims.entries.forEach { sb.append("${it.key}: ${it.value}\n") }
            sb.append(JWTConverter(admins).convert(token))
            sb.toString()
        } else {
            "Anonymous"
        }
    }
}