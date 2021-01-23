import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ConnectionClient {
    suspend fun login(usr: String, psw: String): String {
        val resp = getClient().submitForm<HttpResponse>(
            url = "http://localhost:8080/auth/realms/todolist/protocol/openid-connect/token",
            formParameters = Parameters.build {
                append("client_id", "todolist")
                append("username", usr)
                append("password",psw)
                append("grant_type", "password")
            }

        )
        return resp.readText().split("\"")[3]
    }

    fun getClient(): HttpClient {
        return HttpClient(CIO) {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
    }
}