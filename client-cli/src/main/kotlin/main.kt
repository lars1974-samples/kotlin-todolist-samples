import java.io.File

suspend fun main(args: Array<String>) {
    if(args.isEmpty()) println("Use to do with one of: login, lists, list, item")
    else {
        when(args[0]) {
            "login" -> parseLogin(args)
            "lists" -> parsesLists(args)
            "list" -> parseList(args)
            "p" -> parseMe(args)
        }
    }
}

suspend fun parseLogin(args: Array<String>){
    if(args.size == 1){
        println("todo login [usr] [psw]")
        return
    }
    File("token.txt").writeText(ConnectionClient().login(args[1], args[2]))
}

suspend fun parseMe(args: Array<String>){

    println(getClient().base())

}

suspend fun parseList(args: Array<String>){
    if(args.size == 1){
        println("todo list show [listId]\ntodo list add [listId] [itemText]")
        return
    }
    when(args[1]){
        "show" -> println(TodoListClient("http://localhost:8888", File("token.txt").readText()).listById(args[2].toLong()))
        "add" -> getClient().addItem(args[2].toLong(), args[3])
    }
}

suspend fun parsesLists(args: Array<String>){
    if(args.size == 1){
        println("todo lists show\ntodo lists add [listTitle]")
        return
    }
    when(args[1]){
        "show" -> getClient().listsResume().forEach { println(it.toString()) }
        "add" -> println(getClient().addList(args[2]))
    }
}

fun getClient() = TodoListClient(getUrl(),getToken())

fun getUrl() = "http://localhost:8888"

fun getToken() = File("token.txt").readText()