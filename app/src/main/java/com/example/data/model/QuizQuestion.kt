package com.example.data.model

data class QuizQuestion(
    val id: Int = 0,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val reasonWrong: String,
    val reasonRight: String,
    val userSelectedIndex: Int? = null,
    val isSubmitted: Boolean = false,
    val category: String = "SDLC / Informatika"
) {
    val isCorrect: Boolean
        get() = userSelectedIndex == correctIndex
}
