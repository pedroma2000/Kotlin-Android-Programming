package pt.ipp.estg.quizapp.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import kotlinx.coroutines.launch
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
                questions = questionsList
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

    val context = LocalContext.current

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
            colors = ButtonDefaults.buttonColors( backgroundColor = Color.Blue, contentColor = Color.White),
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
 * Main Screen Component
 */
@Composable
fun MainScreen(questions: List<Question>) {
    var reload by remember { mutableStateOf(false) }
    var questionIndex by remember { mutableStateOf("0") }
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    val handleReload = {
        reload = !reload
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text("QUIZapp!", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = { scope.launch {
                        if(scaffoldState.drawerState.isClosed)
                            scaffoldState.drawerState.open()
                        else
                            scaffoldState.drawerState.close()
                    } }) {
                        Icon( Icons.Filled.Menu, "backIcon", tint = Color.White )
                    }
                },
                backgroundColor = Color.Blue)
                 },
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                questions.forEachIndexed { index, question ->
                    var questionRowModifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                        .height(50.dp)
                        .background(Color.LightGray)
                        .clickable {
                            questionIndex = index.toString()
                            scope.launch { scaffoldState.drawerState.close() }
                        }
                    Question(question, questionRowModifier)
                }
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        questions.forEach { question ->
                            question.isAnswered = "0"
                        }
                        handleReload()
                    }
                ) {
                    Text(
                        text = "RECOMEÇAR DESAFIO",
                        fontSize = 16.sp
                    )
                }
            }
        },
        content = {
            if(questions.get(Integer.parseInt(questionIndex)).isAnswered != "0") {
                AnsweredQuestionScreen(questions = questions, questionIndex = questionIndex)
            } else {
                QuestionScreen(
                    questions = questions,
                    questionIndex = questionIndex,
                    onAnswer = {
                        questions.get(Integer.parseInt(questionIndex)).isAnswered = it

                    }
                )
            }
        }
    )
}

@Composable
fun Question(question: Question, questionRowModifier:Modifier) {
    Row(
        modifier = questionRowModifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 20.dp),
            text = question.question,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(end = 50.dp),
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
fun QuestionScreen(questions: List<Question>, questionIndex: String, onAnswer: (String)->Unit) {
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

/**
 * QuestionScreen Component
 */
@Composable
fun AnsweredQuestionScreen(questions: List<Question>, questionIndex: String) {
    val question = questions.get(Integer.parseInt(questionIndex))

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
        Text(
            text = if (question.isAnswered == "1") "${question.answer}\nResposta Correta!"
            else "Resposta Errada!\nA resposta correta era ${question.answer}",
            modifier = Modifier
                .width(150.dp)
                .height(50.dp),
            color = if(question.isAnswered == "1") Color.Green else Color.Red
        )
    }
}