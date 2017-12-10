package sample.web

import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange


data class Greeting(val message: String)

@RestController
@RequestMapping("/web")
class GreetingController {
    
    @PostMapping("/greet")
    fun handleGreeting(@RequestBody greeting: Greeting): Greeting {
        return Greeting("Thanks: ${greeting.message}")
    }
    
    @GetMapping("/hello")
    fun helloWithFilters(exchange: ServerWebExchange): Greeting {
        return Greeting("${exchange.attributes[KEY]}: Hello")
    }
}