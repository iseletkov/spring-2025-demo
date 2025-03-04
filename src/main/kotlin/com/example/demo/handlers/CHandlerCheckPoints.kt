package com.example.demo.handlers

import com.example.demo.model.CCheckPoint
import com.example.demo.services.CServiceCheckPoints
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import java.util.*


@Component
class CHandlerCheckPoints (
    val serviceCheckPoints                  : CServiceCheckPoints
)
{
    suspend fun getAll(
        request                             : ServerRequest
    )                                       : ServerResponse
    {
        return ServerResponse.ok()
            .json()
            .bodyAndAwait(serviceCheckPoints.findAll())
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
        val res                             = serviceCheckPoints.findById(id)

        res?:run {
            return ServerResponse.notFound()
                .buildAndAwait()
        }

        return ServerResponse.ok()
            .bodyValueAndAwait(res)
    }
    suspend fun saveCheckPoint(
        request                             : ServerRequest
    )                                       : ServerResponse
    {
        val goodInput                       = request.awaitBody<CCheckPoint>()

        //validate(good)
        val goodSaved                       = serviceCheckPoints.save(goodInput)
        return ServerResponse.ok()
            .bodyValueAndAwait(goodSaved)
    }
    suspend fun deleteCheckPointById(
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
        serviceCheckPoints.delete(id)
        return ServerResponse.ok()
            .bodyValueAndAwait("Объект удалён успешно!")
    }
    suspend fun deleteCheckPoint(
        request                             : ServerRequest
    )                                       : ServerResponse
    {
        //Пробуем получить сущность из тела запроса.
        val good                            = request.awaitBody<CCheckPoint>()
        //Если у переданной в теле сущности идентификатор не указан.
        good.id ?: run{
            return ServerResponse.badRequest()
                .bodyValueAndAwait("Без указания идентификатора сущность удалить нельзя!")
        }
        serviceCheckPoints.delete(good.id!!)
        return ServerResponse.ok()
            .bodyValueAndAwait("Объект удалён успешно!")
    }
}