package com.example.demo.handlers

import com.example.demo.services.CServiceCheckPoints
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.io.FileInputStream
import java.util.*


@Component
class CHandlerFiles (
    val serviceCheckPoints                  : CServiceCheckPoints
) {
//    private val minioClient =
//        MinioClient.builder()
//            .endpoint("http://192.168.1.102:50101")
//            .credentials("MobileInspector", "MobileInspector_pass")
//            .build()
    init {


        // Make 'asiatrip' bucket if not exist.
//        val found =
//            minioClient.bucketExists(BucketExistsArgs.builder().bucket("mobileinspector").build())
//        if (!found) {
//            // Make a new bucket called 'asiatrip'.
//            //minioClient.makeBucket(MakeBucketArgs.builder().bucket("mobileinspector").build())
//            println("Bucket 'asiatrip' несуществует.")
//        } else {
//            println("Bucket 'asiatrip' already exists.")
//        }
    }
    suspend fun getById(
        request: ServerRequest
    ): ServerResponse {
        val idStr = request.pathVariable("id")

        val id: UUID
        try {
            id = UUID.fromString(idStr)
        } catch (
            e: Exception
        ) {
//           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return ServerResponse.badRequest()
                .bodyValueAndAwait("Параметр id не соответствует формату UUID v4!")
        }

        val filename = "Кот.png"

        val fileInputStream: FileInputStream = withContext(Dispatchers.IO) {
            FileInputStream(filename)
        }
        val inputStreamResource = InputStreamResource(fileInputStream)

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${filename}\"")
            .bodyValueAndAwait(inputStreamResource)
    }
}