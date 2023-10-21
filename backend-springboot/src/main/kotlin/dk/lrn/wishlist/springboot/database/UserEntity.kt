package dk.lrn.wishlist.springboot.database

import javax.persistence.*

@Entity
data class UserEntity(
    val email: String,
    val name: String,
    @OneToMany(mappedBy = "userId", cascade = [CascadeType.ALL],fetch = FetchType.EAGER)
    var todoLists: MutableList<TodoListEntity>){
    @Id @GeneratedValue var id: Long? = 0

}

