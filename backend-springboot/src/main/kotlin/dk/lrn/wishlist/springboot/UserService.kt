package dk.lrn.wishlist.springboot

import dk.lrn.wishlist.springboot.database.*
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
        val email = getEmailFromToken()
        val user = userRepository.findUserByEmail(email)
        return user?.id ?: createUser(email).id!!
    }

    fun getEmailFromToken(): String{
        val jwt = SecurityContextHolder.getContext().authentication.credentials as Jwt
        return jwt.claims["email"] as String
    }

    fun createUser(email: String): UserEntity {
        userRepository.save(UserEntity(email,"name", mutableListOf()))
        val newUser = userRepository.findUserByEmail(email)!!
        val todoList = TodoListEntity(newUser.id!!,"My First TodoList")
        val saved = todoListRepository.save(todoList)
        TodoItemEntity(saved.id, newUser.id!!,"first item", false)
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