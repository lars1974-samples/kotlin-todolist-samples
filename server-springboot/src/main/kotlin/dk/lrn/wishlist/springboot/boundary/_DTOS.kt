package dk.lrn.wishlist.springboot.boundary


data class TodoListResume(val id: Long?, val name: String?)

data class NewTodoItem(val text: String, val checked: Boolean)

data class TodoItem(val id: Long, val text: String, val checked: Boolean)

data class NewTodoList(val name: String)

data class TodoList(val id: Long, val name: String, var items: MutableList<TodoItem>)



