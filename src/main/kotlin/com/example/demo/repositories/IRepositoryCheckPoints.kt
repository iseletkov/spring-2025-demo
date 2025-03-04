package com.example.demo.repositories

import com.example.demo.model.CCheckPoint
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface IRepositoryCheckPoints : CoroutineCrudRepository<CCheckPoint, UUID> {
}