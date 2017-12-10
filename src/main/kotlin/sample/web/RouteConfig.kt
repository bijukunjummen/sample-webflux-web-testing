package sample.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain


@Configuration
class RouterConfig {

    @Bean
    fun route(): RouterFunction<*> = router {
        POST("/react/greet", { r ->
            val greetingMono = r.bodyToMono<Greeting>()
            greetingMono.flatMap { greeting ->
                ok().body(fromObject(Greeting("Thanks: ${greeting.message}")))
            }
        })

    }

    @Bean
    fun routeWithFilter(): RouterFunction<*> = router {
        GET("/react/hello", { r ->
            ok().body(fromObject(
                    Greeting("${r.attribute(KEY).orElse("[Fallback]: ")}: Hello")
            ))
        })
    }.filter({ r: ServerRequest, n: HandlerFunction<ServerResponse> ->
        val greetings: MutableList<String> = r.attribute(KEY)
                .map { v ->
                    v as MutableList<String>
                }.orElse(mutableListOf())

        greetings.add("From HandlerFilterFunction")

        r.attributes().put(KEY, greetings)
        n.handle(r)
    })

    @Bean
    fun sampleWebFilter(): WebFilter {
        return WebFilter { e: ServerWebExchange, c: WebFilterChain ->
            val l: MutableList<String> = e.getAttributeOrDefault(KEY, mutableListOf())
            l.add("From WebFilter")
            e.attributes.put(KEY, l)
            c.filter(e)
        }
    }
}