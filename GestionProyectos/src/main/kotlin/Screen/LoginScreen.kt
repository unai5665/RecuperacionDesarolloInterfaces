package Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import network.apiLogIn
import theme.*

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var usuario by remember { mutableStateOf("") }
        var passwd by remember { mutableStateOf("") }

        // Fondo degradado con tonos fríos
        val gradientColors = listOf(Color(0xFF1E3C72), Color(0xFF2A5298))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.verticalGradient(colors = gradientColors))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = 15.dp,
                    backgroundColor = Color(0xFF0D47A1)
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFF1565C0))
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Log in",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            value = usuario,
                            onValueChange = { usuario = it },
                            label = { Text("Username", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(0.8f),
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.White,
                                focusedBorderColor = Color(0xFF42A5F5),
                                unfocusedBorderColor = Color.White,
                                focusedLabelColor = Color(0xFF42A5F5),
                                cursorColor = Color.White
                            )
                        )
                        OutlinedTextField(
                            value = passwd,
                            onValueChange = { passwd = it },
                            label = { Text("Password", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(0.8f),
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            // Aquí se oculta la contraseña
                            visualTransformation = PasswordVisualTransformation(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.White,
                                focusedBorderColor = Color(0xFF42A5F5),
                                unfocusedBorderColor = Color.White,
                                focusedLabelColor = Color(0xFF42A5F5),
                                cursorColor = Color.White
                            )
                        )
                        Button(
                            onClick = {
                                if (usuario.isNotEmpty() && passwd.isNotEmpty()) {
                                    apiLogIn(usuario, passwd) { user ->
                                        usuario = ""
                                        passwd = ""
                                        navigator?.push(WelcomeScreen(user))
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(0.8f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF42A5F5))
                        ) {
                            Text("Iniciar Sesión", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
