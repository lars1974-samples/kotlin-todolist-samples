package dk.lrn.wishlist.springboot.contract

import dk.lrn.wishlist.springboot.boundary.*
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*

@SecurityRequirement(name = "bearerAuth")
@RequestMapping("todo")
interface TodoEndpointInterface {
    @GetMapping("/lists")
    fun getListsResumes(): List<TodoListResume>

    @GetMapping("/lists/{listId}")
    fun getList(@PathVariable("listId") listId: Long): TodoList

    @PostMapping("/lists")
    fun addList(@RequestBody newTodoList: NewTodoList)

    @PostMapping("/lists/{listId}")
    fun addItemToList(@PathVariable("listId") listId: Long, @RequestBody itemEntity: NewTodoItem)

    @DeleteMapping("/lists/{listId}/{itemId}")
    fun removeItem(@PathVariable("listId") listId: Long, @PathVariable("itemId") itemId: Long)

    @PutMapping("/lists/{listId}/{itemId}")
    fun updateItem(@PathVariable("listId") listId: Long, @PathVariable("itemId") itemId: Long, @RequestBody todoItem: NewTodoItem)
}