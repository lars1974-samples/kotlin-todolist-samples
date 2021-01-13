package dk.lajktor.exposed.client

import dk.laj.ktor.exposed.dao.CityDto
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    Restclient().createCity("Copenhagen")
    Restclient().createCity("Lyngby")
}

class Restclient {
    val client = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = GsonSerializer {
                // .GsonBuilder

            }
        }
    }

    fun createCity(name: String) {
        runBlocking {
            val city = client.post<CityDto> {
                url("http://127.0.0.1:8080/cities")
                contentType(ContentType.Application.Json)
                body = CityDto(0, name)
            }
            client.close()
        }
    }
}