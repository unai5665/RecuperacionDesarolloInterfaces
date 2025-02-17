package Screen

import modelo.Proyecto
import modelo.User
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import network.apiObtenerProyectos
import network.apiObtenerProyectosMios
import theme.*

class ProyectosScreen(val user: User) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        // Listas de proyectos
        val proyectsList = remember { mutableStateOf(emptyList<Proyecto>()) }
        apiObtenerProyectos {
            proyectsList.value = it
        }

        val misProyectsList = remember { mutableStateOf(emptyList<Proyecto>()) }
        apiObtenerProyectosMios(user.idGestor) {
            misProyectsList.value = it
        }

        // Estado para la opción seleccionada: 0 para "Todos", 1 para "Mios"
        var selectedIndex by remember { mutableStateOf(0) }
        // Lista mostrada según la opción seleccionada
        val proyectosMostrados = remember { mutableStateOf<List<Proyecto>>(emptyList()) }
        LaunchedEffect(selectedIndex, proyectsList.value, misProyectsList.value) {
            proyectosMostrados.value = if (selectedIndex == 0) {
                proyectsList.value
            } else {
                misProyectsList.value
            }
        }

        // Fondo degradado en tonos fríos
        val backgroundGradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298))
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Proyectos", color = Color.White) },
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
                // Contenedor central con ancho limitado y espaciado
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .align(Alignment.TopCenter)
                ) {
                    // Encabezado con título y control segmentado
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = Color(0xFF0D47A1),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Proyectos",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            SegmentedControl(
                                selectedIndex = selectedIndex,
                                onIndexChanged = { selectedIndex = it }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    // Lista de proyectos
                    if (proyectosMostrados.value.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(proyectosMostrados.value) { proyecto ->
                                ProyectoItem(proyecto)
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay proyectos disponibles", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProyectoItem(proyecto: Proyecto) {
    val navigator = LocalNavigator.current
    // Alterna colores para diferenciar elementos
    val backgroundColor = if (proyecto.id % 2 == 0) {
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
            Text("${proyecto.id}", fontWeight = FontWeight.Bold, color = Color.Black)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = proyecto.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = proyecto.descripcion,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            Button(
                onClick = { navigator?.push(ProyectoScreen(proyecto)) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1565C0))
            ) {
                Text("Ver proyecto", color = Color.White)
            }
        }
    }
}

@Composable
fun SegmentedControl(selectedIndex: Int, onIndexChanged: (Int) -> Unit) {
    val options = listOf("Todos", "Mios")
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                onClick = { onIndexChanged(index) },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

@Composable
fun SegmentedButton(
    onClick: () -> Unit,
    selected: Boolean,
    label: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (selected) Color(0xFF1565C0) else Color.Transparent,
            contentColor = Color.White
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        label()
    }
}
