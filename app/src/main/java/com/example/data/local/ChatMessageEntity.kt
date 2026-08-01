package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.ChatMessage

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val hasQuizLink: Boolean = false,
    val quizLinkText: String? = null,
    val attachedFileName: String? = null,
    val attachedFileType: String? = null
) {
    fun toDomain(): ChatMessage = ChatMessage(
        id = id,
        text = text,
        isFromUser = isFromUser,
        timestamp = timestamp,
        hasQuizLink = hasQuizLink,
        quizLinkText = quizLinkText,
        attachedFileName = attachedFileName,
        attachedFileType = attachedFileType
    )

    companion object {
        fun fromDomain(domain: ChatMessage): ChatMessageEntity = ChatMessageEntity(
            id = domain.id,
            text = domain.text,
            isFromUser = domain.isFromUser,
            timestamp = domain.timestamp,
            hasQuizLink = domain.hasQuizLink,
            quizLinkText = domain.quizLinkText,
            attachedFileName = domain.attachedFileName,
            attachedFileType = domain.attachedFileType
        )
    }
}
