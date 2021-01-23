package dk.lrn.wishlist.springboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam

@SpringBootApplication
class WishlistSpringbootApplication

fun main(args: Array<String>) {
    runApplication<WishlistSpringbootApplication>(*args)

}

@RestController
class Hello(
    val userRepository: UserRepository,
    val todoListRepository: TodoListRepository,
    val listItemRepository: ListItemRepository
){

    @GetMapping("/")
    fun getUser(@AuthenticationPrincipal jwt: Jwt): User {
        val user = userRepository.findUserByName(getId(jwt))
        val todoList = TodoList(null, "My First TodoList", mutableListOf(TodoListItem(null,"first item", false)))
        if(user == null) userRepository.save(User(null,getId(jwt), mutableListOf(todoList)))

       return userRepository.findUserByName(getId(jwt))!!

    }

    @GetMapping("/lists")
    fun getLists(@AuthenticationPrincipal jwt: Jwt): List<TodoList> {
        return getUser(jwt).todoLists
    }

    @GetMapping("/lists-resume")
    fun getListsResume(@AuthenticationPrincipal jwt: Jwt): List<TodoListResume> {
        return getUser(jwt).todoLists.map { TodoListResume(it.id, it.name) }
    }

    data class TodoListResume(val id: Long?, val name: String?)

    @GetMapping("/lists/{listid}")
    fun getLists(@AuthenticationPrincipal jwt: Jwt, @PathVariable("listid") listid: Long): TodoList {
        return getUser(jwt).todoLists.find { it.id == listid }!!
    }

    @PostMapping("/lists")
    fun addList(@AuthenticationPrincipal jwt: Jwt, @RequestBody listItem: TodoList){
        val list = getUser(jwt)
        list.todoLists.add(TodoList(null,listItem.name, mutableListOf()))
        userRepository.save(list)
    }

    @PostMapping("/lists/{listid}")
    fun addItemToList(@AuthenticationPrincipal jwt: Jwt, @PathVariable("listid") listid: Long, @RequestBody listItem: TodoListItem){
        val list = getUser(jwt).todoLists.find { it.id == listid }!!
        list.itemTodos.add(listItem)
        todoListRepository.save(list)
    }

    @DeleteMapping("/lists/{listid}/{itemid}")
    fun removeItem(@AuthenticationPrincipal jwt: Jwt, @PathVariable("itemid") itemid: Long, @PathParam("listid") listid: Long){
        val list = getUser(jwt).todoLists.find { it.id == listid }!!
        val item = list.itemTodos.find { it.id == itemid }!!
        list.itemTodos.remove(item)
        todoListRepository.save(list)
    }

    @PutMapping("/lists/{listid}")
    fun updateItem(@AuthenticationPrincipal jwt: Jwt, @PathParam("listid") listid: Long, @RequestBody listItem: TodoListItem){
        val list = getUser(jwt).todoLists.find { it.id == listid }!!
        val item = list.itemTodos.find { it.id == listItem.id }!!
        item.description = listItem.description
        item.done = listItem.done
        listItemRepository.save(item)
    }













    @GetMapping("/token")
    fun getToken(@AuthenticationPrincipal jwt: Jwt): String {

        return jwt.id
    }

    @PostMapping("/users")
    fun addUser(@RequestBody user: User){
        userRepository.save(user)
    }

    fun getId(jwt: Jwt): String {
        return jwt.claims["email"] as String
    }

}
