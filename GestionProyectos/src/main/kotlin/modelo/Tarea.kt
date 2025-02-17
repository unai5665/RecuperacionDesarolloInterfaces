package modelo

import kotlinx.serialization.Serializable

@Serializable
data class Tarea(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val estimacion: Int,
    val fecha_creacion: String,
    val fecha_finalizacion: String?,
    val programador: Int,
    val proyecto: Int
)