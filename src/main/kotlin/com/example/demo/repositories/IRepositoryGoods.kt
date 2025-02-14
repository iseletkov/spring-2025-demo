package com.example.demo.repositories

import com.example.demo.model.CGood
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface IRepositoryGoods : CoroutineCrudRepository<CGood, UUID> {

}
