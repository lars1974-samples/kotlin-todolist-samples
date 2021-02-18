package dk.lrn.wishlist.springboot.boundary

import dk.lrn.wishlist.springboot.database.TodoListEntity
import dk.lrn.wishlist.springboot.TodoItemEntity
import dk.lrn.wishlist.springboot.UserService
import dk.lrn.wishlist.springboot.contract.TodoEndpointInterface
import dk.lrn.wishlist.springboot.database.TodoItemRepository
import dk.lrn.wishlist.springboot.database.TodoListRepository
import org.springframework.web.bind.annotation.RestController

@RestController
class TodoEndpoint(
    val todoListRepository: TodoListRepository,
    val todoItemRepository: TodoItemRepository,
    val userService: UserService
) : TodoEndpointInterface {
    override fun getListsResumes(): List<TodoListResume> {
        return todoListRepository.findByUserId(userService.getUserId()).map { TodoListResume(it.id, it.name) }
    }

    override fun getList(listId: Long): TodoList {
        val listEntity = todoListRepository.getByUserIdAndId(userService.getUserId(), listId)
        return TodoList(listEntity.id, listEntity.name, listEntity.itemTodoEntities.map { TodoItem(it.id, it.description, it.done) }.toMutableList())
    }

    override fun addList(newTodoList: NewTodoList) {
        val todoListEntity = TodoListEntity(userService.getUserId(), newTodoList.name, mutableListOf())
        todoListRepository.save(todoListEntity)
    }

    override fun addItemToList(listId: Long, itemEntity: NewTodoItem) {
        val userId = userService.getUserId()
        val list = todoListRepository.getByUserIdAndId(userId, listId)
        list.itemTodoEntities.add(TodoItemEntity(userId,itemEntity.text, false))
        todoListRepository.save(list)
    }

    override fun removeItem(listId: Long, itemId: Long) {
        val userId = userService.getUserId()
        val itemEntity = todoItemRepository.getByUserIdAndId(userId, listId)
        todoItemRepository.delete(itemEntity)
    }

    override fun updateItem(listId: Long, itemId: Long, todoItem: NewTodoItem) {
        val userId = userService.getUserId()
        val itemEntity = todoItemRepository.getByUserIdAndId(userId, itemId)
        itemEntity.description = todoItem.text
        itemEntity.done = todoItem.checked
        todoItemRepository.save(itemEntity)
    }
}