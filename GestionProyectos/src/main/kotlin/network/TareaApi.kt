package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelo.LoginRequest
import modelo.Tarea
import modelo.TareaRequest
import modelo.User
import network.NetworkUtils.httpClient
import utils.sha512


fun apiObtenerTareas(id: Int, onSuccessResponse: (List<Tarea>) -> Unit) {
    val url = "http://127.0.0.1:5000/proyecto/tareas_proyectos?id=$id"

    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.get(url) {
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.OK) {
            val listTareas = response.body<List<Tarea>>()
            onSuccessResponse(listTareas)
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}


fun apiAsignarTarea(nuevaTarea: TareaRequest, onSuccessResponse: (Tarea) -> Unit) {
    val url = "http://127.0.0.1:5000/proyecto/tarea_proyecto"
    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(nuevaTarea)
        }
        if (response.status == HttpStatusCode.OK) {
            val tarea = response.body<Tarea>()
            onSuccessResponse(tarea)
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}
