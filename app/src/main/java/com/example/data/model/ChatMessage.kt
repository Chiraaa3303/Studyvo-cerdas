package com.example.data.model

data class ChatMessage(
    val id: Long = 0,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val hasQuizLink: Boolean = false,
    val quizLinkText: String? = null,
    val attachedFileName: String? = null,
    val attachedFileType: String? = null // "PDF", "FOTO", "FILE"
)
