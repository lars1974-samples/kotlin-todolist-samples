package dk.lrn.wishlist.springboot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dk.lrn.wishlist.springboot.boundary.NewTodoList
import dk.lrn.wishlist.springboot.database.TodoItemRepository
import dk.lrn.wishlist.springboot.database.TodoListEntity
import dk.lrn.wishlist.springboot.database.TodoListRepository
import dk.lrn.wishlist.springboot.database.UserRepository
import org.hamcrest.Matchers.`is`
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TodoEndpointTest() {
    @Autowired lateinit var mvc: MockMvc
    @SpyBean lateinit var userService: UserService
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var totoListRepository: TodoListRepository
    @Autowired lateinit var todoItemRepository: TodoItemRepository

    @Test
    @Throws(Exception::class)
    fun exampleTest() {
        println(userRepository.count())
        Mockito.doReturn("newuser@dk").`when`(userService).getEmailFromToken()
        mvc.perform(get("/todo/lists"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name", `is`("My First TodoList")))
        println(userRepository.count())
    }

    @Test
    @Throws(Exception::class)
    fun testCreateListAndRemove() {
        Mockito.doReturn("newuser@dk").`when`(userService).getEmailFromToken()
        mvc.perform(post("/todo/lists").contentType(MediaType.APPLICATION_JSON).content(jacksonObjectMapper().writeValueAsString(NewTodoList("JUNIT"))))
            .andExpect(status().isOk)


        mvc.perform(get("/todo/lists"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[1].name", `is`("JUNIT")))

      //  val resp = mvc.perform(get("/todo/lists")).andReturn().response.contentAsString

        mvc.perform(get("/todo/lists"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", `is`(2)))
    }

    @Test
    fun testThatUserIsDeleted(){

        Mockito.doReturn("testThatUserIsDeleted@dk").`when`(userService).getEmailFromToken()
        val userId = userService.getUserId()
        mvc.perform(post("/todo/lists").contentType(MediaType.APPLICATION_JSON).content(jacksonObjectMapper().writeValueAsString(NewTodoList("JUNIT"))))

        mvc.perform(get("/todo/lists"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", `is`(2)))

        mvc.perform(delete("/todo/users/${userId}")).andExpect(status().isOk)

        Assert.assertTrue(totoListRepository.findByUserId(userId).isEmpty())
        Assert.assertTrue(todoItemRepository.findByUserId(userId).isEmpty())
    }
}
