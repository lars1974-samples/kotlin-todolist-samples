package dk.laj.ktor.exposed.dao

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.jackson.*
import io.ktor.features.*
import kotlin.test.*
import io.ktor.server.testing.*

class ApplicationTest {
    @Test
    fun testCreateCity() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Post, "/cities") {
                addHeader("Content-Type", "application/json")
                setBody(jacksonObjectMapper().writeValueAsString(CityDto(0,"Stubbekoebing")))
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                //assertEquals("HELLO WORLD!", response.content)
            }
        }
    }


    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/cities").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                //assertEquals("HELLO WORLD!", response.content)
            }
        }
    }
}
