package com.example.activities.ui.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.example.activities.ui.screens.SecondActivity

@Composable
fun MainActivityComp(context: Context) {
    Column() {
        var input by remember { mutableStateOf(TextFieldValue("")) }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Escreva um texto")
        TextField(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            value = input,
            onValueChange = { input = it })
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                Toast.makeText(context , input.text, Toast.LENGTH_LONG).show()
            }) {
            Text(text = "Apresentar Toast")
        }
        val intent = Intent(context, SecondActivity::class.java)
        intent.putExtra("value", input.text)
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = { context.startActivity(intent) }) {
            Text(text = "Abrir Nova Activitie")
        }
    }
}

@Composable
fun SecondActivityComp(text: String) {
    Column() {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "$text")
    }
}