package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.ui.components.AttachmentSheetModal
import com.example.ui.components.QuickActionCard
import com.example.ui.theme.ChipBrown
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.InputBarBrown
import com.example.ui.theme.RubyBackgroundDark
import com.example.ui.theme.RubySurfaceCard
import com.example.ui.theme.RubySurfaceElevated
import com.example.ui.theme.TextWhite
import com.example.ui.theme.TextWhiteSecondary
import com.example.ui.viewmodel.StudyvoViewModel

@Composable
fun StudyvoFlashScreen(
    viewModel: StudyvoViewModel,
    messages: List<ChatMessage>,
    isGenerating: Boolean,
    onOpenExercises: () -> Unit
) {
    val listState = rememberLazyListState()
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    val isSheetVisible = viewModel.attachmentSheetVisible

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RubyBackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Heading: "Apa yang Dapat Saya Bantu?" (Screenshot 1 & 2)
            Text(
                text = "Apa yang Dapat Saya Bantu?",
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 14.dp)
            )

            // 3 Quick Action Cards (Screenshot 1)
            QuickActionCard(
                text = "mengubah materi menjadi soal soal",
                onClick = { viewModel.onQuickActionSelected("mengubah materi menjadi soal soal") }
            )
            Spacer(modifier = Modifier.height(10.dp))

            QuickActionCard(
                text = "mengubah materi menjadi rangkuman",
                onClick = { viewModel.onQuickActionSelected("mengubah materi menjadi rangkuman") }
            )
            Spacer(modifier = Modifier.height(10.dp))

            QuickActionCard(
                text = "mengubah soal menjadi materi",
                onClick = { viewModel.onQuickActionSelected("mengubah soal menjadi materi") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable Chat Messages
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    ChatBubbleItem(
                        message = msg,
                        onSpeakClick = { viewModel.speakMessage(msg.text, msg.id) },
                        onCopyClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Studyvo", msg.text)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Teks disalin!", Toast.LENGTH_SHORT).show()
                        },
                        onQuizLinkClick = onOpenExercises
                    )
                }

                if (isGenerating) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(RubySurfaceCard)
                                .padding(14.dp)
                        ) {
                            CircularProgressIndicator(
                                color = CoralAccent,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Studyvo sedang menyusun materi / soal...",
                                color = TextWhiteSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Bottom Input Capsule Bar ("Ketik Perintah...")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(InputBarBrown)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Attachment + Button (Screenshot 1-7)
                IconButton(onClick = { viewModel.showAttachmentSheet() }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Lampirkan",
                        tint = TextWhite,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Center Input TextField
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = {
                        Text(
                            text = "Ketik Perintah...",
                            color = TextWhiteSecondary,
                            fontSize = 16.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        cursorColor = CoralAccent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.weight(1f),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendUserMessage(inputText)
                                inputText = ""
                            }
                        }
                    )
                )

                // Right Send / Voice Wave Icon
                IconButton(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            viewModel.sendUserMessage(inputText)
                            inputText = ""
                        } else {
                            viewModel.sendUserMessage("Buatkan latihan soal bab ini")
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (inputText.isNotBlank()) Icons.Default.Send else Icons.Default.GraphicEq,
                        contentDescription = "Kirim / Suara",
                        tint = TextWhite,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }

        // Attachment Bottom Sheet Drawer (Screenshot 5)
        AnimatedVisibility(visible = isSheetVisible.value) {
            AttachmentSheetModal(
                onDismiss = { viewModel.hideAttachmentSheet() },
                onPhotoClick = { viewModel.attachSamplePhoto() },
                onCameraClick = { viewModel.attachSamplePhoto() },
                onFileClick = { viewModel.attachSamplePdf() }
            )
        }
    }
}

@Composable
fun ChatBubbleItem(
    message: ChatMessage,
    onSpeakClick: () -> Unit,
    onCopyClick: () -> Unit,
    onQuizLinkClick: () -> Unit
) {
    val isUser = message.isFromUser

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Message Card Bubble
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .background(if (isUser) RubySurfaceElevated else RubySurfaceCard)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column {
                // Attached file preview card if present (e.g. PDF card from Screenshot 6)
                if (message.attachedFileName != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(ChipBrown)
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "PDF",
                            tint = CoralAccent,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = message.attachedFileType ?: "PDF",
                                color = TextWhiteSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = message.attachedFileName,
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Text body
                Text(
                    text = message.text,
                    color = TextWhite,
                    fontSize = 15.sp,
                    lineHeight = 21.sp
                )

                // Clickable Quiz Link Chip (Screenshot 7: https//linksoal.adalahpokoknya)
                if (message.hasQuizLink && !message.quizLinkText.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(ChipBrown)
                            .clickable(onClick = onQuizLinkClick)
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = message.quizLinkText,
                                color = CoralAccent,
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onQuizLinkClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CoralAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Buka Latihan Soal (Studyvo Exercises) →",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // Action icons under AI messages (Screenshot 3, 4, 6, 7, 13)
        if (!isUser) {
            Row(
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconAction(icon = Icons.Default.ThumbUp, contentDescription = "Suka")
                IconAction(icon = Icons.Default.ThumbDown, contentDescription = "Tidak Suka")
                IconAction(icon = Icons.Default.Refresh, contentDescription = "Ulangi")
                IconAction(icon = Icons.Default.ContentCopy, contentDescription = "Salin", onClick = onCopyClick)
                IconAction(icon = Icons.Default.MoreHoriz, contentDescription = "Lainnya")
                IconAction(
                    icon = Icons.Default.VolumeUp,
                    contentDescription = "Bicara",
                    tint = CoralAccent,
                    onClick = onSpeakClick
                )
            }
        }
    }
}

@Composable
private fun IconAction(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    tint: Color = TextWhiteSecondary,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(24.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
    }
}
