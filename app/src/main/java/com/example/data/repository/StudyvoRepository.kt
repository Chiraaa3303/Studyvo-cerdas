package com.example.data.repository

import com.example.data.local.ChatMessageEntity
import com.example.data.local.QuizDao
import com.example.data.local.QuizQuestionEntity
import com.example.data.model.ChatMessage
import com.example.data.model.QuizQuestion
import com.example.data.remote.GeminiAiService
import com.example.data.sample.SampleData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class StudyvoRepository(private val quizDao: QuizDao) {

    val allQuestions: Flow<List<QuizQuestion>> = quizDao.getAllQuestions().map { entities ->
        entities.map { it.toDomain() }
    }

    val allMessages: Flow<List<ChatMessage>> = quizDao.getAllMessages().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun initializeDefaultDataIfNeeded() {
        val currentQuestions = quizDao.getAllQuestions().firstOrNull()
        if (currentQuestions.isNullOrEmpty()) {
            val entities = SampleData.defaultQuestions.map { QuizQuestionEntity.fromDomain(it) }
            quizDao.insertQuestions(entities)
        }

        val currentMessages = quizDao.getAllMessages().firstOrNull()
        if (currentMessages.isNullOrEmpty()) {
            for (msg in SampleData.defaultChatMessages) {
                quizDao.insertMessage(ChatMessageEntity.fromDomain(msg))
            }
        }
    }

    suspend fun saveQuestions(questions: List<QuizQuestion>) {
        quizDao.deleteAllQuestions()
        val entities = questions.map { QuizQuestionEntity.fromDomain(it) }
        quizDao.insertQuestions(entities)
    }

    suspend fun updateQuestion(question: QuizQuestion) {
        quizDao.updateQuestion(QuizQuestionEntity.fromDomain(question))
    }

    suspend fun resetAllQuizAnswers() {
        val current = quizDao.getAllQuestions().firstOrNull() ?: return
        val resetList = current.map {
            it.copy(userSelectedIndex = null, isSubmitted = false)
        }
        quizDao.insertQuestions(resetList)
    }

    suspend fun addChatMessage(message: ChatMessage): Long {
        return quizDao.insertMessage(ChatMessageEntity.fromDomain(message))
    }

    suspend fun clearChatHistory() {
        quizDao.deleteAllMessages()
    }

    suspend fun convertMaterialToQuestions(material: String, apiKey: String): List<QuizQuestion> {
        val result = GeminiAiService.convertMaterialToQuestions(material, apiKey)
        saveQuestions(result)
        return result
    }

    suspend fun convertMaterialToSummary(material: String, apiKey: String): String {
        return GeminiAiService.convertMaterialToSummary(material, apiKey)
    }

    suspend fun convertQuestionsToMaterial(questionText: String, apiKey: String): String {
        return GeminiAiService.convertQuestionsToMaterial(questionText, apiKey)
    }
}
