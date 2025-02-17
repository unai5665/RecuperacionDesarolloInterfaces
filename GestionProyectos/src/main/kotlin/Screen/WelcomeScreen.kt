package Screen

import modelo.Proyecto
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import modelo.User
import network.apiObtenerHistorial
import theme.*

class WelcomeScreen(val user: User) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val historyList = remember { mutableStateOf(emptyList<Proyecto>()) }
        apiObtenerHistorial {
            historyList.value = it
        }

        // Fondo degradado en tonos fríos
        val backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298))
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Bienvenido, ${user.nombre}") },
                    backgroundColor = Color(0xFF1565C0),
                    contentColor = Color.White
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
                // Contenedor central con ancho limitado
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Sección principal: dos columnas en una Row sin usar intrinsics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Columna izquierda: Proyectos activos
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            backgroundColor = Color(0xFF0D47A1),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Proyectos activos",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Button(
                                    onClick = { navigator?.push(ProyectosScreen(user)) },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF42A5F5))
                                ) {
                                    Text("Ver proyectos", color = Color.White)
                                }
                            }
                        }
                        // Columna derecha: Historial
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            backgroundColor = Color(0xFF1565C0),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "Historial",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                if (historyList.value.isNotEmpty()) {
                                    // Se le asigna fillMaxHeight para ocupar el espacio disponible sin intrinsics
                                    LazyColumn(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        items(historyList.value) { proyecto ->
                                            ProyectoFinItem(proyecto)
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Sin historial", color = Color.White)
                                    }
                                }
                            }
                        }
                    }

                    // Botón de Cerrar Sesión
                    Button(
                        onClick = { navigator?.pop() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF42A5F5))
                    ) {
                        Text("Cerrar Sesión", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ProyectoFinItem(proyecto: Proyecto) {
    val backgroundColor = if (proyecto.id % 2 == 0) {
        Color(0xFF64B5F6)
    } else {
        Color(0xFFBBDEFB)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = proyecto.nombre,
            fontSize = 16.sp,
            color = Color.Black
        )
        proyecto.fecha_finalizacion?.let { fecha ->
            Text(text = fecha, fontSize = 14.sp, color = Color.Red)
        }
    }
}
