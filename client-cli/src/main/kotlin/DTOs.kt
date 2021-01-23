data class TodoListItem(val id: Long, val description: String,val done: Boolean){
    override fun toString() = "%-20s  %-30s".format(id, description)
}

data class TodoList (val id: Long, val name: String, val itemTodos: MutableList<TodoListItem>){
    override fun toString(): String {
        var s = "%-20s  %-30s\n----------------------------------------------------\n".format(id, name)
        itemTodos.forEach { s += "%-20s  %-30s\n".format(it.id, it.description) }
        return s
    }
}

data class TodoListResume(val id: Long, val name: String){
    override fun toString() = "%-20s  %-30s".format(id, name)
}