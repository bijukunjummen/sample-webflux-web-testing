package sample.web

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.HttpHeaders.ACCEPT
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters


@RunWith(SpringRunner::class)
@WebFluxTest(controllers = arrayOf(GreetingController::class, RouterConfig::class))
class GreetingControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun testHandleGreetings() {
        webTestClient.post()
                .uri("/web/greet")
                .header(CONTENT_TYPE, "application/json")
                .body(BodyInserters
                        .fromObject(""" 
                                |{
                                |   "message": "Hello Web"
                                |}
                            """.trimMargin()))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json("""
                    |{
                    |   "message": "Thanks: Hello Web"
                    |}
                """.trimMargin())
    }

    @Test
    fun testAnnotationEndpointsWithFilters() {
        webTestClient.get()
                .uri("/web/hello")
                .header(ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .json("""
                    |{
                    |   "message": "[From WebFilter]: Hello"
                    |}
                """.trimMargin())
    }
}