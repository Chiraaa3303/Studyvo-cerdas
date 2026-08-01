package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    // --- Quiz Questions ---
    @Query("SELECT * FROM quiz_questions ORDER BY id ASC")
    fun getAllQuestions(): Flow<List<QuizQuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestionEntity>)

    @Update
    suspend fun updateQuestion(question: QuizQuestionEntity)

    @Query("DELETE FROM quiz_questions")
    suspend fun deleteAllQuestions()

    // --- Chat Messages ---
    @Query("SELECT * FROM chat_messages ORDER BY id ASC")
    fun getAllMessages(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity): Long

    @Query("DELETE FROM chat_messages")
    suspend fun deleteAllMessages()
}
