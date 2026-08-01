package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.StudyvoMode
import com.example.ui.components.StudyvoTopBar
import com.example.ui.screens.StudyvoExercisesScreen
import com.example.ui.screens.StudyvoFlashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RubyBackgroundDark
import com.example.ui.viewmodel.StudyvoViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: StudyvoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                StudyvoApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun StudyvoApp(viewModel: StudyvoViewModel) {
    val currentMode by viewModel.currentMode.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val quizQuestions by viewModel.quizQuestions.collectAsStateWithLifecycle()
    val expandedReasons by viewModel.expandedReasons.collectAsStateWithLifecycle()
    val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = RubyBackgroundDark,
        topBar = {
            StudyvoTopBar(
                currentMode = currentMode,
                onModeSelect = { selectedMode ->
                    viewModel.switchMode(selectedMode)
                },
                onMenuClick = {
                    viewModel.clearChat()
                },
                onRightActionClick = {
                    viewModel.toggleMode()
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(RubyBackgroundDark)
        ) {
            when (currentMode) {
                StudyvoMode.FLASH -> {
                    StudyvoFlashScreen(
                        viewModel = viewModel,
                        messages = chatMessages,
                        isGenerating = isGenerating,
                        onOpenExercises = {
                            viewModel.switchMode(StudyvoMode.EXERCISES)
                        }
                    )
                }
                StudyvoMode.EXERCISES -> {
                    StudyvoExercisesScreen(
                        questions = quizQuestions,
                        expandedReasons = expandedReasons,
                        onSelectAnswer = { qId, optIdx ->
                            viewModel.selectAnswer(qId, optIdx)
                        },
                        onToggleReason = { qId ->
                            viewModel.toggleReason(qId)
                        },
                        onResetQuiz = {
                            viewModel.resetQuiz()
                        },
                        onBackToFlash = {
                            viewModel.switchMode(StudyvoMode.FLASH)
                        }
                    )
                }
            }
        }
    }
}
