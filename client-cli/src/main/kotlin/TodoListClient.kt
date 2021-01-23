import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class TodoListClient(val baseUrl: String, val token: String) {
    suspend fun listById(id: Long): TodoList {
        return getClient().get("$baseUrl/lists/${id}") {
            header("Authorization", "Bearer $token")
        }
    }

    suspend fun listsResume(): List<TodoListResume> {
        return getClient().get("$baseUrl/lists-resume") {
            header("Authorization", "Bearer $token")
        }
    }

    suspend fun addItem(id: Long, s: String) {
        getClient().post<HttpResponse>("$baseUrl/lists/$id") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
            body = TodoListItem(0, s, false)
        }
    }

    suspend fun addList(s: String) {
        getClient().post<HttpResponse>("$baseUrl/lists") {
            contentType(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
            body = TodoList(0, s, mutableListOf())
        }
    }

    fun getClient(): HttpClient {
        return HttpClient(CIO) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
    }
}