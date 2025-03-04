package com.example.demo.services

import com.example.demo.model.CCheckPoint
import com.example.demo.model.CGood
import com.example.demo.repositories.IRepositoryCheckPoints
import com.example.demo.repositories.IRepositoryGoods
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import java.util.*

@Service
class CServiceCheckPoints(
    val repository: IRepositoryCheckPoints
) {
    suspend fun findById(id: UUID): CCheckPoint? =
        repository.findById(id)

    suspend fun findAll(): Flow<CCheckPoint> =
        repository.findAll()

    suspend fun save(
        checkpoint                                : CCheckPoint
    )                                       : CCheckPoint
    {
        if (checkpoint.id==null)
        {
            checkpoint.id                   = UUID.randomUUID()
            checkpoint.new                  = true
        }
        else
        {
            checkpoint.new                  = !(repository.existsById(checkpoint.id!!))
        }

        return repository.save(checkpoint)
    }

    suspend fun delete(
        id                                  : UUID
    )
    {
        return repository.deleteById(id)
    }
}