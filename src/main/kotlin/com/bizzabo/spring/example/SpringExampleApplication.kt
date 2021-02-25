package com.bizzabo.spring.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


@SpringBootApplication
class SpringExampleApplication

fun main(args: Array<String>) {
    runApplication<SpringExampleApplication>(*args)
}

@Component
class GreetingHandler {

    fun hello(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_PLAIN)
            .bodyValue("Hello, Spring!")
    }
}

@Configuration
class GreetingRouter {

    @Bean
    fun route(greetingHandler: GreetingHandler): RouterFunction<ServerResponse> {

        return RouterFunctions
            .route(RequestPredicates.GET("/hello")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), HandlerFunction { greetingHandler.hello(it) })
    }
}
