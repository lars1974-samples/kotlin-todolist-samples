package dk.lrn.wishlist.springboot.boundary

import dk.lrn.wishlist.springboot.database.TodoListEntity
import dk.lrn.wishlist.springboot.database.TodoItemEntity
import dk.lrn.wishlist.springboot.UserService
import dk.lrn.wishlist.springboot.contract.TodoEndpointInterface
import dk.lrn.wishlist.springboot.database.TodoItemRepository
import dk.lrn.wishlist.springboot.database.TodoListRepository
import dk.lrn.wishlist.springboot.database.UserRepository
import org.springframework.web.bind.annotation.RestController

@RestController
class TodoEndpoint(
    val todoListRepository: TodoListRepository,
    val todoItemRepository: TodoItemRepository,
    val userService: UserService,
    val userRepository: UserRepository
) : TodoEndpointInterface {
    override fun getListsResumes(): List<TodoListResume> {
        return todoListRepository.findByUserId(userService.getUserId()).map { TodoListResume(it.id, it.name) }
    }

    override fun getList(listId: Long): TodoList {
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa")
        val listEntity = todoListRepository.getByUserIdAndId(userService.getUserId(), listId)
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa")
        val todoList = TodoList(listEntity.id, listEntity.name, listEntity.itemTodoEntities.map { TodoItem(it.id, it.description, it.done) }.toMutableList())
        println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
        return todoList
    }

    override fun deleteUser(userId: Long) {
        userRepository.deleteById(userService.getUserId())
    }

    override fun deleteList(listId: Long) {
        val list = todoListRepository.getByUserIdAndId(userService.getUserId(), listId)
        todoListRepository.delete(list)
    }

    override fun addList(newTodoList: NewTodoList) {
        val todoListEntity = TodoListEntity(userService.getUserId(), newTodoList.name)
        todoListRepository.save(todoListEntity)
    }

    override fun addItemToList(listId: Long, itemEntity: NewTodoItem) {
        todoItemRepository.save(TodoItemEntity(listId, userService.getUserId(),itemEntity.text, false))
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