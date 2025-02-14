package com.example.demo.services

import com.example.demo.model.CGood
import com.example.demo.repositories.IRepositoryGoods
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import java.util.*


@Service
class CServiceGoods(
    val repository: IRepositoryGoods
) {
    suspend fun findById(id: UUID): CGood? =
        repository.findById(id)

    suspend fun findAll(): Flow<CGood> =
        repository.findAll()

    suspend fun save(
        good                                : CGood
    )                                       : CGood
    {
        if (good.id==null)
        {
            good.id                         = UUID.randomUUID()
            good.new                        = true
        }
        else
        {
            good.new                        = !(repository.existsById(good.id!!))
        }

        return repository.save(good)
    }

    suspend fun delete(
        id                                  : UUID
    )
    {
        return repository.deleteById(id)
    }
}