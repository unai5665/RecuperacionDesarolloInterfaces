package network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import modelo.LoginRequest
import modelo.Proyecto
import modelo.User
import network.NetworkUtils.httpClient
import utils.sha512


fun apiObtenerHistorial(onSuccessResponse: (List<Proyecto>) -> Unit) {
    val url = "http://127.0.0.1:5000/proyecto/historial"

    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.get(url) {
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.OK) {
            val listProyecto = response.body<List<Proyecto>>()
            onSuccessResponse(listProyecto)
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}

fun apiObtenerProyectos(onSuccessResponse: (List<Proyecto>) -> Unit) {
    val url = "http://127.0.0.1:5000/proyecto/proyectos_activos"

    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.get(url) {
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.OK) {
            val listProyecto = response.body<List<Proyecto>>()
            onSuccessResponse(listProyecto)
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}


fun apiObtenerProyectosMios(id: Int, onSuccessResponse: (List<Proyecto>) -> Unit) {
    val url = "http://127.0.0.1:5000/proyecto/proyectos_gestor?id=$id"

    CoroutineScope(Dispatchers.IO).launch {
        val response = httpClient.get(url) {
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.OK) {
            val listProyecto = response.body<List<Proyecto>>()
            onSuccessResponse(listProyecto)
        } else {
            println("Error: ${response.status}, Body: ${response.bodyAsText()}")
        }
    }
}

