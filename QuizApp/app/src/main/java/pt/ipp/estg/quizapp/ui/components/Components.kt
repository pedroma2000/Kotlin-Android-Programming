package pt.ipp.estg.quizapp.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.ipp.estg.quizapp.models.Question

@Composable
fun MainActivityComp(questions: List<Question>) {
    Column() {
        for (question in questions) {
            Question(question)
        }
    }
}

@Composable
fun Question(question: Question) {
    val context = LocalContext.current
    var input by remember { mutableStateOf(TextFieldValue(""))}
    val isCorrect = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .padding(start = 20.dp) ,
            text = question.question,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.fillMaxWidth(0.2f),
            text = if (isCorrect.value) "✔" else "",
            color = Color.Green
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .width(100.dp)
                    .padding(end = 10.dp)
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.9f),
                value = input,
                singleLine = true,
                readOnly = (isCorrect.value),
                shape = RoundedCornerShape(2.dp),
                onValueChange = {input = it})
            Button(
                modifier = Modifier
                    .width(100.dp)
                    .padding(end = 10.dp)
                    .weight(0.4f)
                    .fillMaxWidth(0.7f),
                onClick = {
                    var toastText = "Wrong Answer! Try Again"
                    if(input.text == question.answer) {
                        isCorrect.value = true
                        toastText = "Correct!"
                    }

                    Toast.makeText(context,toastText, Toast.LENGTH_LONG).show()
            },
                enabled = (!isCorrect.value),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isCorrect.value) Color.Green else Color.Blue,
                    contentColor = Color.White)
            )
            {
                Text(text = if (isCorrect.value) "Feito!" else "Verificar")
            }
        }
    }
}