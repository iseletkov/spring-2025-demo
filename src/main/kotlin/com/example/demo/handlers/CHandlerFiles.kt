package com.example.demo.handlers

import com.example.demo.services.CServiceCheckPoints
import io.minio.GetObjectArgs
import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


@Component
class CHandlerFiles (
    val serviceCheckPoints                  : CServiceCheckPoints
) {
    @Value("\${minio.bucket.name}")
    private lateinit var bakcetname         : String

    private val minioClient =
        MinioClient.builder()
            .endpoint("http://192.168.1.102:50101")
            .credentials("mobileinspector", "mobileinspector_pass")
            .build()
    init {


        // Make 'asiatrip' bucket if not exist.

            //minioClient.bucketExists(BucketExistsArgs.builder().bucket("mobileinspector").build())
//        minioClient.listBuckets()
//            .forEach {bucket ->
//                println("Bucket '${bucket.name()}' не существует.")
//            }

//        if (!found) {
//            // Make a new bucket called 'asiatrip'.
//            //minioClient.makeBucket(MakeBucketArgs.builder().bucket("mobileinspector").build())
//            println("Bucket 'asiatrip' не существует.")
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

        print("Имя корзины: $bakcetname")

        val filename = "/files/$id.jpg"

        //Дополнительное логирование запросов к minio
        //minioClient.traceOn(System.out)

        //Здесь скачивание из MinIO в оперативную память сервера.
        //В файловую систему не сохранять - работает долго.
        val fileBytes: ByteArray = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket("mobileinspector")
                .`object`(filename)
                .build()
        ).use { stream ->
            convertInputStreamToByteArray(stream)
        }


        //Формирование ответа на запрос на скачивание файла.
        ByteArrayInputStream(fileBytes)
            .use{bais ->
                val inputStreamResource = InputStreamResource(bais)

                return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"${filename}\"")
                    .bodyValueAndAwait(inputStreamResource)
            }

    }
    @Throws(IOException::class)
    fun convertInputStreamToByteArray(inputStream: InputStream) : ByteArray
    {
        // Используем ByteArrayOutputStream для записи данных из потока
        val buffer: ByteArrayOutputStream = ByteArrayOutputStream()
        val data = ByteArray(1024)
        var bytesRead: Int

        // Читаем данные из потока и записываем в буфер
        while ((inputStream.read(data, 0, data.size).also { bytesRead = it }) != -1) {
            buffer.write(data, 0, bytesRead)
        }

        // Преобразуем буфер в массив байтов
        return buffer.toByteArray()
    }
}