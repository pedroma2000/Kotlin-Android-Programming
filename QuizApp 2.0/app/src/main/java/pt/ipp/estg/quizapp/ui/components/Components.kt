package pt.ipp.estg.quizapp.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pt.ipp.estg.quizapp.models.Question

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "splashScreen",
    questionsList: List<Question>
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("splashScreen") {
            SplashScreen(onNavigateToMainScreen = { navController.navigate("mainScreen") })
        }
        composable("mainScreen") {
            MainScreen(
                questions = questionsList,
                onNavigateToQuestionScreen = {
                    navController.navigate("questionScreen/$it")
                },
                refreshScreen = { navController.navigate("mainScreen") }
            )
        }
        composable("questionScreen/{questionId}", listOf(navArgument("questionId") { type = NavType.StringType })
        ) {
            navBackStackEntry ->
            QuestionScreen(
                questions = questionsList,
                questionIndex = navBackStackEntry.arguments?.getString("questionId").toString(),
                onNavigateToMainScreen = { navController.navigate("mainScreen") },
                onAnswer = {
                    questionsList.get(Integer.parseInt(navBackStackEntry.arguments?.getString("questionId").toString())).isAnswered = it
                }
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
}

/**
 * Splash Screen Component
 */
@Composable
fun SplashScreen(onNavigateToMainScreen: ()->Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 100.dp, bottom = 20.dp),
            text = "QUIZapp!",
            fontSize = 60.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Bem Vindo",
            fontSize = 30.sp,
            fontFamily = FontFamily.Monospace,
        )
        Button(
            modifier = Modifier.padding(top = 200.dp),
            onClick = onNavigateToMainScreen
        ) {
            Text(
                text = "ENTRAR",
                fontSize = 20.sp
            )
        }
    }
}

/**
 * MainScreen Component
 */
@Composable
fun MainScreen(questions: List<Question>, onNavigateToQuestionScreen: (questionIndex:String)->Unit, refreshScreen : ()->Unit) {
    var handleRefresh by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .padding(bottom = 10.dp, top = 10.dp),
            text = "Selecione uma pergunta",
            fontSize = 22.sp,
            fontWeight = FontWeight.W700,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var i = 0
            questions.forEachIndexed { index, question ->
                var questionRowModifier = Modifier
                    .fillMaxWidth(0.97f)
                    .padding(bottom = 20.dp)
                    .height(50.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .clickable {
                        if (question.isAnswered == "0") {
                            onNavigateToQuestionScreen(index.toString())
                        }
                    }
                Question(question, questionRowModifier)
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.5f),
        ) {
            Button(
                modifier = Modifier.align(Alignment.Center),
                onClick = {
                    questions.forEach { question ->
                        question.isAnswered = "0"
                    }
                    refreshScreen()
                }
            ) {
                Text(
                    text = "RECOMEÇAR DESAFIO",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun Question(question: Question, questionRowModifier:Modifier) {
    val context = LocalContext.current
    Row(
        modifier = questionRowModifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(start = 20.dp),
            text = question.question,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(end = 20.dp),
            text = if (question.isAnswered == "1") "✔ Correto" else if (question.isAnswered == "-1") "❌ Errado" else "",
            color = if (question.isAnswered == "1") Color.Green else Color.Red,
            fontSize = 20.sp
        )
    }
}

/**
 * QuestionScreen Component
 */
@Composable
fun QuestionScreen(questions: List<Question>, questionIndex: String, onNavigateToMainScreen: ()->Unit, onAnswer: (String)->Unit) {
    val question = questions.get(Integer.parseInt(questionIndex))
    var input by remember { mutableStateOf(TextFieldValue(""))}
    var answer = "-1"
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 100.dp, bottom = 100.dp),
            text = question.question,
            fontSize = 50.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold
        )
        TextField(
            modifier = Modifier
                .width(150.dp)
                .height(50.dp),
            value = input,
            singleLine = true,
            shape = RoundedCornerShape(2.dp),
            onValueChange = {input = it},
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
        Button(
            modifier = Modifier
                .padding(top = 50.dp)
                .width(200.dp)
                .height(50.dp),
            onClick = {
                var toastText = "Wrong Answer!"
                if(input.text == "") {
                    answer = "0"
                } else if(input.text == question.answer) {
                    answer = "1"
                    toastText = "Correct!"
                }
                Toast.makeText(context,toastText, Toast.LENGTH_LONG).show()

                if(answer != "0") {
                    onAnswer(answer)
                    onNavigateToMainScreen()
                }
            },
            colors = ButtonDefaults.buttonColors( backgroundColor = Color.Blue, contentColor = Color.White)
        )
        {
            Text(
                text = "Confirmar Resposta",
                fontSize = 16.sp
            )
        }
    }
}