package dk.lrn.wishlist.springboot.database

import javax.persistence.*

@Entity
data class UserEntity(val email: String, val name: String){
    @Id @GeneratedValue var id: Long? = 0
}

