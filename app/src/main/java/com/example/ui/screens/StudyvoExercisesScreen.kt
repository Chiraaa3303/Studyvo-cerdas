package com.example.ui.screens

import android.content.Context
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuizQuestion
import com.example.ui.theme.CorrectGreenCard
import com.example.ui.theme.ReasonBoxBackground
import com.example.ui.theme.RubyBackgroundDark
import com.example.ui.theme.RubySurfaceCard
import com.example.ui.theme.RubySurfaceElevated
import com.example.ui.theme.TextWhite
import com.example.ui.theme.TextWhiteSecondary
import com.example.ui.theme.WrongRedCard

@Composable
fun StudyvoExercisesScreen(
    questions: List<QuizQuestion>,
    expandedReasons: Set<Int>,
    onSelectAnswer: (questionId: Int, optionIndex: Int) -> Unit,
    onToggleReason: (questionId: Int) -> Unit,
    onResetQuiz: () -> Unit,
    onBackToFlash: () -> Unit
) {
    val context = LocalContext.current
    val answeredCount = questions.count { it.isSubmitted }
    val correctCount = questions.count { it.isSubmitted && it.isCorrect }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RubyBackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
        ) {
            // Score & Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Latihan Soal Interaktif",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Skor: $correctCount dari $answeredCount terjawab benar",
                        color = TextWhiteSecondary,
                        fontSize = 14.sp
                    )
                }

                OutlinedButton(
                    onClick = onResetQuiz,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                    border = BorderStroke(1.dp, TextWhiteSecondary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Reset", fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Scrollable list of Question Cards (Screenshots 8-12)
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(questions, key = { it.id }) { question ->
                    QuestionCardItem(
                        question = question,
                        isReasonExpanded = expandedReasons.contains(question.id),
                        onOptionSelected = { index ->
                            // Vibrate haptic feedback
                            try {
                                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                                vibrator?.vibrate(30)
                            } catch (e: Exception) {
                                // ignore
                            }
                            onSelectAnswer(question.id, index)
                        },
                        onReasonClick = {
                            onToggleReason(question.id)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onBackToFlash,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RubySurfaceElevated,
                            contentColor = TextWhite
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = "⚡ Kembali ke Studyvo Flash (AI Chat)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun QuestionCardItem(
    question: QuizQuestion,
    isReasonExpanded: Boolean,
    onOptionSelected: (Int) -> Unit,
    onReasonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = RubySurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Question text header (Screenshot 8)
            Text(
                text = question.questionText,
                color = TextWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Options list with radio circles
            question.options.forEachIndexed { index, optionText ->
                val isSelected = question.userSelectedIndex == index
                val isCorrectIndex = question.correctIndex == index
                val isSubmitted = question.isSubmitted

                val optionBackgroundColor = when {
                    !isSubmitted -> Color.Transparent
                    isCorrectIndex -> CorrectGreenCard
                    isSelected && !isCorrectIndex -> WrongRedCard
                    else -> Color.Transparent
                }

                val circleBorderColor = when {
                    !isSubmitted -> TextWhite
                    isCorrectIndex -> Color.White
                    isSelected && !isCorrectIndex -> Color.White
                    else -> TextWhiteSecondary
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(optionBackgroundColor)
                        .clickable { onOptionSelected(index) }
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Radio circle
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                color = when {
                                    isSubmitted && isCorrectIndex -> Color.White
                                    isSubmitted && isSelected && !isCorrectIndex -> Color.White
                                    else -> Color.Transparent
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSubmitted && isCorrectIndex) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Benar",
                                tint = CorrectGreenCard,
                                modifier = Modifier.size(16.dp)
                            )
                        } else if (isSubmitted && isSelected && !isCorrectIndex) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Salah",
                                tint = WrongRedCard,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            // Empty outline circle
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(Color.Transparent)
                                    .padding(2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color.Transparent)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Option Text + "(jawaban yang benar)" if green
                    val annotatedString = buildAnnotatedString {
                        append(optionText)
                        if (isSubmitted && isCorrectIndex) {
                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFC8E6C9)
                                )
                            ) {
                                append(" (jawaban yang benar)")
                            }
                        }
                    }

                    Text(
                        text = annotatedString,
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = if (isSelected || isCorrectIndex) FontWeight.Bold else FontWeight.Normal,
                        lineHeight = 21.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ALASAN... v Button (Screenshot 9, 10, 11)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onReasonClick)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ALASAN...",
                        color = TextWhiteSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (isReasonExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand Reason",
                        tint = TextWhiteSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Expandable Reason Box (Screenshot 11-12)
            AnimatedVisibility(
                visible = isReasonExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ReasonBoxBackground)
                        .padding(18.dp)
                ) {
                    // Why Wrong explanation
                    Text(
                        text = question.reasonWrong,
                        color = TextWhite,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Why Right explanation
                    Text(
                        text = question.reasonRight,
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
