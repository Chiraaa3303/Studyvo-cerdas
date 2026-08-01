package com.example.ui.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.QuizDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.QuizQuestion
import com.example.data.model.StudyvoMode
import com.example.data.repository.StudyvoRepository
import com.example.data.sample.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class StudyvoViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val repository: StudyvoRepository
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _currentMode = MutableStateFlow(StudyvoMode.FLASH)
    val currentMode: StateFlow<StudyvoMode> = _currentMode.asStateFlow()

    private val _expandedReasons = MutableStateFlow<Set<Int>>(emptySet())
    val expandedReasons: StateFlow<Set<Int>> = _expandedReasons.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _attachmentSheetVisible = MutableStateFlow(false)
    val attachmentSheetVisible: StateFlow<Boolean> = _attachmentSheetVisible.asStateFlow()

    private val _ttsSpeakingId = MutableStateFlow<Long?>(null)
    val ttsSpeakingId: StateFlow<Long?> = _ttsSpeakingId.asStateFlow()

    private var currentActiveCommand: String = "mengubah materi menjadi soal soal"

    val chatMessages: StateFlow<List<ChatMessage>>
    val quizQuestions: StateFlow<List<QuizQuestion>>

    init {
        val database = QuizDatabase.getDatabase(application)
        repository = StudyvoRepository(database.quizDao())

        chatMessages = repository.allMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        quizQuestions = repository.allQuestions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.initializeDefaultDataIfNeeded()
        }

        try {
            tts = TextToSpeech(application, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("id", "ID"))
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsReady = true
            }
        }
    }

    fun switchMode(mode: StudyvoMode) {
        _currentMode.value = mode
    }

    fun toggleMode() {
        _currentMode.value = if (_currentMode.value == StudyvoMode.FLASH) {
            StudyvoMode.EXERCISES
        } else {
            StudyvoMode.FLASH
        }
    }

    fun showAttachmentSheet() {
        _attachmentSheetVisible.value = true
    }

    fun hideAttachmentSheet() {
        _attachmentSheetVisible.value = false
    }

    /**
     * Called when user taps one of the 3 quick action buttons on Studyvo Flash:
     * - "mengubah materi menjadi soal soal"
     * - "mengubah materi menjadi rangkuman"
     * - "mengubah soal menjadi materi"
     */
    fun onQuickActionSelected(actionText: String) {
        currentActiveCommand = actionText
        viewModelScope.launch {
            repository.addChatMessage(
                ChatMessage(
                    text = actionText,
                    isFromUser = true
                )
            )

            // AI replies: "siap! tolong kirim materinya ya" (Screenshot 3 & 4)
            repository.addChatMessage(
                ChatMessage(
                    text = "siap! tolong kirim materinya ya",
                    isFromUser = false
                )
            )
        }
    }

    /**
     * Called when user types text in bottom bar or sends material
     */
    fun sendUserMessage(
        text: String,
        attachedFileName: String? = null,
        attachedFileType: String? = null,
        apiKey: String = ""
    ) {
        if (text.isBlank() && attachedFileName == null) return
        viewModelScope.launch {
            _isGenerating.value = true

            // Add user message
            repository.addChatMessage(
                ChatMessage(
                    text = if (text.isNotBlank()) text else "Melampirkan berkas materi pembelajaran",
                    isFromUser = true,
                    attachedFileName = attachedFileName,
                    attachedFileType = attachedFileType
                )
            )

            // Determine conversion output
            val contentToProcess = if (text.isNotBlank()) text else SampleData.sampleMaterialText

            when (currentActiveCommand) {
                "mengubah materi menjadi soal soal" -> {
                    // Generate questions
                    repository.convertMaterialToQuestions(contentToProcess, apiKey)

                    // Reply with quiz link chip (Screenshot 7)
                    repository.addChatMessage(
                        ChatMessage(
                            text = "Berikut tautan latihan soal interaktif yang sudah saya siapkan dari materimu:",
                            isFromUser = false,
                            hasQuizLink = true,
                            quizLinkText = "https//linksoal.adalahpokoknya"
                        )
                    )

                    // Follow up closing message (Screenshot 13)
                    repository.addChatMessage(
                        ChatMessage(
                            text = "Gimana? apa itu membantu? Jika ada perintah lain silahkan hubungi saya lagi! semangat belajarnya ya!!",
                            isFromUser = false
                        )
                    )
                }
                "mengubah materi menjadi rangkuman" -> {
                    val summary = repository.convertMaterialToSummary(contentToProcess, apiKey)
                    repository.addChatMessage(
                        ChatMessage(
                            text = summary,
                            isFromUser = false
                        )
                    )
                    repository.addChatMessage(
                        ChatMessage(
                            text = "Gimana? apa itu membantu? Jika ada perintah lain silahkan hubungi saya lagi! semangat belajarnya ya!!",
                            isFromUser = false
                        )
                    )
                }
                "mengubah soal menjadi materi" -> {
                    val explanation = repository.convertQuestionsToMaterial(contentToProcess, apiKey)
                    repository.addChatMessage(
                        ChatMessage(
                            text = explanation,
                            isFromUser = false
                        )
                    )
                    repository.addChatMessage(
                        ChatMessage(
                            text = "Gimana? apa itu membantu? Jika ada perintah lain silahkan hubungi saya lagi! semangat belajarnya ya!!",
                            isFromUser = false
                        )
                    )
                }
                else -> {
                    // Default to creating practice questions
                    repository.convertMaterialToQuestions(contentToProcess, apiKey)
                    repository.addChatMessage(
                        ChatMessage(
                            text = "Berikut tautan latihan soal interaktif yang sudah saya siapkan:",
                            isFromUser = false,
                            hasQuizLink = true,
                            quizLinkText = "https//linksoal.adalahpokoknya"
                        )
                    )
                    repository.addChatMessage(
                        ChatMessage(
                            text = "Gimana? apa itu membantu? Jika ada perintah lain silahkan hubungi saya lagi! semangat belajarnya ya!!",
                            isFromUser = false
                        )
                    )
                }
            }

            _isGenerating.value = false
        }
    }

    /**
     * Simulates clicking "file" in attachment drawer (Screenshot 5 -> 6):
     * Inserts "PDF - penilaian bab 8 da..." and converts to quiz!
     */
    fun attachSamplePdf(apiKey: String = "") {
        hideAttachmentSheet()
        sendUserMessage(
            text = "Tolong ubah file PDF penilaian bab 8 ini menjadi latihan soal pilihan ganda ya.",
            attachedFileName = "penilaian bab 8 da...",
            attachedFileType = "PDF",
            apiKey = apiKey
        )
    }

    /**
     * Simulates clicking "foto" or "kamera" in attachment drawer
     */
    fun attachSamplePhoto(apiKey: String = "") {
        hideAttachmentSheet()
        sendUserMessage(
            text = "Tolong jelaskan materi dari catatan foto soal ini.",
            attachedFileName = "catatan_soal_bab8.jpg",
            attachedFileType = "FOTO",
            apiKey = apiKey
        )
    }

    /**
     * Called when user answers a multiple choice question in Studyvo Exercises
     */
    fun selectAnswer(questionId: Int, selectedOptionIndex: Int) {
        viewModelScope.launch {
            val currentList = quizQuestions.value
            val target = currentList.find { it.id == questionId } ?: return@launch
            val updated = target.copy(
                userSelectedIndex = selectedOptionIndex,
                isSubmitted = true
            )
            repository.updateQuestion(updated)

            // Automatically expand reason box when answered (Screenshots 11-12)
            _expandedReasons.value = _expandedReasons.value + questionId
        }
    }

    fun toggleReason(questionId: Int) {
        val current = _expandedReasons.value
        _expandedReasons.value = if (current.contains(questionId)) {
            current - questionId
        } else {
            current + questionId
        }
    }

    fun resetQuiz() {
        viewModelScope.launch {
            repository.resetAllQuizAnswers()
            _expandedReasons.value = emptySet()
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
            repository.initializeDefaultDataIfNeeded()
        }
    }

    /**
     * Triggers Android TTS engine to read out the assistant response (Speaker icon 🔊)
     */
    fun speakMessage(text: String, messageId: Long) {
        if (tts == null) return
        if (_ttsSpeakingId.value == messageId) {
            tts?.stop()
            _ttsSpeakingId.value = null
            return
        }
        val cleanText = text.replace("**", "")
            .replace("#", "")
            .replace("https//linksoal.adalahpokoknya", "tautan soal latihan")
        _ttsSpeakingId.value = messageId
        tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "TTS_$messageId")
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
