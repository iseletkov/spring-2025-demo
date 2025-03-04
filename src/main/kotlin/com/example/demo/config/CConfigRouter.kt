package com.example.demo.config

import com.example.demo.handlers.CHandlerCheckPoints
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

    @Bean
    fun routesCheckPoint(
        handlerCheckPoints                        : CHandlerCheckPoints
    )                                       = coRouter  {
        GET("/checkpoints", handlerCheckPoints::getAll )
        GET("/checkpoints/{id}", handlerCheckPoints::getById )
        POST("/checkpoints", handlerCheckPoints::saveCheckPoint)
        DELETE("/checkpoints/{id}", handlerCheckPoints::deleteCheckPointById)
        DELETE("/checkpoints", handlerCheckPoints::deleteCheckPoint)
        onError<Exception> { e, _ -> badRequest()
            .bodyValueAndAwait("Ошибка на стороне сервера.\n"+e.message+"\n"+e.stackTrace.joinToString(separator = "\n"))
        }
    }
}