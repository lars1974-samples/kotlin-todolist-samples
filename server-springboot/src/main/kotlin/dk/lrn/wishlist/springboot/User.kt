package dk.lrn.wishlist.springboot

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
data class User(
    @Id @GeneratedValue var id: Long? = null,
    var name: String?,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    var todoLists: MutableList<TodoList>

)

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun findUserByName(user: String): User?

}