package Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import modelo.Proyecto
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import modelo.Tarea
import modelo.TareaRequest
import network.apiAsignarTarea
import network.apiObtenerTareas
import theme.*

class ProyectoScreen(proyecto: Proyecto): Screen {

    val proyectoSeleccionado = proyecto

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        val tareasList = remember { mutableStateOf(emptyList<Tarea>()) }
        apiObtenerTareas(proyectoSeleccionado.id) {
            tareasList.value = it
        }

        var showDialog by remember { mutableStateOf(false) }
        var nombreTarea by remember { mutableStateOf("") }
        var descripcionTarea by remember { mutableStateOf("") }
        var estimacion by remember { mutableStateOf(0) }
        var fechaCreacion by remember { mutableStateOf("") }
        var fechaFinalizacion by remember { mutableStateOf("") }
        var programador by remember { mutableStateOf(0) }

        // Fondo degradado con tonos fríos
        val backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298))
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Proyecto", color = Color.White) },
                    backgroundColor = Color(0xFF1565C0)
                )
            },
            backgroundColor = Color.Transparent
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundGradient)
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Información del proyecto
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = Color(0xFF0D47A1),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = proyectoSeleccionado.nombre,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = proyectoSeleccionado.descripcion,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = proyectoSeleccionado.fecha_creacion,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                proyectoSeleccionado.fecha_finalizacion?.let {
                                    Text(
                                        text = it,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sección de Tareas
                    if (tareasList.value.isNotEmpty()) {
                        Text(
                            "Tareas",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(tareasList.value) { tarea ->
                                TareaItem(tarea)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay tareas asignadas", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sección para asignar tarea
                    Text(
                        "Asignar Tarea",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF42A5F5))
                    ) {
                        Text("Asignar Tarea", color = Color.White)
                    }

                    // Diálogo para asignar tarea
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Asignar Nueva Tarea") },
                            text = {
                                Column {
                                    OutlinedTextField(
                                        value = nombreTarea,
                                        onValueChange = { nombreTarea = it },
                                        label = { Text("Nombre de la Tarea") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF42A5F5),
                                            textColor = Color.White,
                                            cursorColor = Color.White,
                                            focusedLabelColor = Color(0xFF42A5F5)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = descripcionTarea,
                                        onValueChange = { descripcionTarea = it },
                                        label = { Text("Descripción") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF42A5F5),
                                            textColor = Color.White,
                                            cursorColor = Color.White,
                                            focusedLabelColor = Color(0xFF42A5F5)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = estimacion.toString(),
                                        onValueChange = { estimacion = it.toIntOrNull() ?: 0 },
                                        label = { Text("Estimación (horas)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF42A5F5),
                                            textColor = Color.White,
                                            cursorColor = Color.White,
                                            focusedLabelColor = Color(0xFF42A5F5)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = fechaCreacion,
                                        onValueChange = { fechaCreacion = it },
                                        label = { Text("Fecha de Creación") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF42A5F5),
                                            textColor = Color.White,
                                            cursorColor = Color.White,
                                            focusedLabelColor = Color(0xFF42A5F5)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = fechaFinalizacion,
                                        onValueChange = { fechaFinalizacion = it },
                                        label = { Text("Fecha de Finalización") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF42A5F5),
                                            textColor = Color.White,
                                            cursorColor = Color.White,
                                            focusedLabelColor = Color(0xFF42A5F5)
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = programador.toString(),
                                        onValueChange = { programador = it.toIntOrNull() ?: 0 },
                                        label = { Text("Programador ID") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            focusedBorderColor = Color(0xFF42A5F5),
                                            textColor = Color.White,
                                            cursorColor = Color.White,
                                            focusedLabelColor = Color(0xFF42A5F5)
                                        )
                                    )
                                }
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        val nuevaTarea = TareaRequest(
                                            nombreTarea,
                                            descripcionTarea,
                                            estimacion,
                                            fechaCreacion,
                                            fechaFinalizacion,
                                            programador,
                                            proyectoSeleccionado.id
                                        )
                                        apiAsignarTarea(nuevaTarea) {
                                            // Actualiza la lista de tareas tras asignar la nueva tarea.
                                            apiObtenerTareas(proyectoSeleccionado.id) {
                                                tareasList.value = it
                                            }
                                        }
                                        showDialog = false
                                    }
                                ) {
                                    Text("Añadir", color = Color(0xFF42A5F5))
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showDialog = false }
                                ) {
                                    Text("Cancelar", color = Color.Red)
                                }
                            },
                            backgroundColor = Color(0xFF1565C0),
                            contentColor = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TareaItem(tarea: Tarea) {
    val navigator = LocalNavigator.current
    val backgroundColor = if (tarea.id % 2 == 0) {
        Color(0xFF64B5F6)
    } else {
        Color(0xFFBBDEFB)
    }
    Card(
        shape = RoundedCornerShape(10.dp),
        backgroundColor = backgroundColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarea.nombre,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tarea.descripcion,
                    color = Color.Black
                )
            }
            Button(
                onClick = { navigator?.push(TareaScreen(tarea)) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1565C0))
            ) {
                Text("Ver tarea", color = Color.White)
            }
        }
    }
}
