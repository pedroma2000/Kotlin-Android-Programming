package pt.ipp.estg.quizapp.models

data class Question(val question:String, val answer:String, var isAnswered:String = "0")
