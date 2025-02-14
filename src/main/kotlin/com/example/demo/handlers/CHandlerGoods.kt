package com.example.demo.handlers

import com.example.demo.model.CGood
import com.example.demo.services.CServiceGoods
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.util.*

@Component
class CHandlerGoods (
    val serviceGoods                        : CServiceGoods
)
{
    suspend fun getAll(
        request                             : ServerRequest
    )                                       : ServerResponse
    {
        return ok()
            .json()
            .bodyAndAwait(serviceGoods.findAll())
    }
    suspend fun getById(
        request                             : ServerRequest
    )                                       : ServerResponse
    {
        val idStr                           = request.pathVariable("id")

        val id                              : UUID
        try {
            id                              = UUID.fromString(idStr)
        }
        catch(
            e                               : Exception
        )
        {
            return ServerResponse.badRequest()
                .bodyValueAndAwait("Параметр id не соответствует формату UUID v4!")
        }
        val res                             = serviceGoods.findById(id)

        res?:run {
            return ServerResponse.notFound()
                .buildAndAwait()
        }

        return ok()
            .bodyValueAndAwait(res)
    }
    suspend fun saveGood(
        request                             : ServerRequest
    )                                       : ServerResponse
    {
        val goodInput                       = request.awaitBody<CGood>()

        //validate(good)
        val goodSaved                       = serviceGoods.save(goodInput)
        return ok()
            .bodyValueAndAwait(goodSaved)
    }
    suspend fun deleteGoodById(
        request                             : ServerRequest
    )                                       : ServerResponse
    {
        //Пробуем получить идентификатор из пути запроса.
        val idStr                           = request.pathVariable("id")
        val id                              = try {
                //Проверяем корректность формата идентификатора
            UUID.fromString(idStr)
        } catch (
            e: Exception
        ) {
            return ServerResponse.badRequest()
                .bodyValueAndAwait("Параметр id не соответствует формату UUID v4!")
        }
        serviceGoods.delete(id)
        return ok()
            .bodyValueAndAwait("Объект удалён успешно!")
    }
    suspend fun deleteGood(
        request                             : ServerRequest
    )                                       : ServerResponse
    {
        //Пробуем получить сущность из тела запроса.
        val good                            = request.awaitBody<CGood>()
        //Если у переданной в теле сущности идентификатор не указан.
        good.id ?: run{
            return ServerResponse.badRequest()
                .bodyValueAndAwait("Без указания идентификатора сущность удалить нельзя!")
        }
        serviceGoods.delete(good.id!!)
        return ok()
            .bodyValueAndAwait("Объект удалён успешно!")
    }
}