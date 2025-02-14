package com.example.demo.config

import com.example.demo.handlers.CHandlerGoods
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

//functional endpoints
@Configuration
class CConfigRouter {
    @Bean
    fun goodRoutes(
        handlerGoods                        : CHandlerGoods
    )                                       = coRouter  {
        GET("/goods", handlerGoods::getAll )
        GET("/goods/{id}", handlerGoods::getById )
        POST("/goods", handlerGoods::saveGood)
        DELETE("/goods/{id}", handlerGoods::deleteGoodById)
        DELETE("/goods", handlerGoods::deleteGood)
        onError<Exception> { e, _ -> badRequest()
            .bodyValueAndAwait("Ошибка на стороне сервера.\n"+e.message+"\n"+e.stackTrace.joinToString(separator = "\n"))
        }
    }
}