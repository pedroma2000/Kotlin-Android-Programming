package pt.ipp.estg.dialogs

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import pt.ipp.estg.dialogs.ui.theme.DialogsTheme

@Composable
fun MainScreen() {
    var isLogged by remember { mutableStateOf(false) }
    val validEmail = "pedro@gmail.com"
    val validPassword = "123456"
    val ctx = LocalContext.current

    if(!isLogged) {
        LoginDialog(login = { email, password ->
            if(email == validEmail && password == validPassword) {
                isLogged = true
                Toast.makeText(ctx, "Login efetuado com sucesso", Toast.LENGTH_LONG).show()
            }
            Toast.makeText(ctx, "Ocorreu um erro durante o login", Toast.LENGTH_LONG).show()
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "BEM VINDO",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Username:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Pedro",
                fontSize = 14.sp,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Email:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = validEmail,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun LoginDialog(login: (email:String, password:String) -> Unit) {
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 5.dp),
                    text = "LOGIN",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
                TextField(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .height(50.dp),
                    placeholder = { Text(text = "Email") },
                    value = email,
                    onValueChange = { email = it }
                )
                TextField(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .height(50.dp),
                    placeholder = { Text(text = "Password") },
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = {passwordVisible = !passwordVisible}){
                            Icon(imageVector  = image, description)
                        }
                    }
                )
                Button(
                    onClick = { login(email,password) }
                ) {
                    Text(text = "Login")
                }
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun Preview() {
    DialogsTheme {
        LoginDialog(login = { email, password ->  })
    }
}