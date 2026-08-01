package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.QuizQuestion

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val reasonWrong: String,
    val reasonRight: String,
    val userSelectedIndex: Int? = null,
    val isSubmitted: Boolean = false,
    val category: String = "SDLC / Informatika"
) {
    fun toDomain(): QuizQuestion = QuizQuestion(
        id = id,
        questionText = questionText,
        options = options,
        correctIndex = correctIndex,
        reasonWrong = reasonWrong,
        reasonRight = reasonRight,
        userSelectedIndex = userSelectedIndex,
        isSubmitted = isSubmitted,
        category = category
    )

    companion object {
        fun fromDomain(domain: QuizQuestion): QuizQuestionEntity = QuizQuestionEntity(
            id = domain.id,
            questionText = domain.questionText,
            options = domain.options,
            correctIndex = domain.correctIndex,
            reasonWrong = domain.reasonWrong,
            reasonRight = domain.reasonRight,
            userSelectedIndex = domain.userSelectedIndex,
            isSubmitted = domain.isSubmitted,
            category = domain.category
        )
    }
}
