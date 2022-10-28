package pt.ipp.estg.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import pt.ipp.estg.quizapp.models.Question
import pt.ipp.estg.quizapp.ui.theme.QuizAppTheme
import pt.ipp.estg.quizapp.ui.components.MainActivityComp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val question1 = Question("1 + 1", "2")
                    val question2 = Question("3 + 7", "10")
                    val question3 = Question("35 + 16", "51")
                    val question4 = Question("215 + 387", "602")
                    val question5 = Question("1056 + 784", "1830")
                    val questions = listOf(question1, question2, question3, question4, question5)

                    MainActivityComp(questions)
                }
            }
        }
    }
}