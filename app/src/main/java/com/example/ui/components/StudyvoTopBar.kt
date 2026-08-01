package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyvoMode
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.RubyBackgroundDark
import com.example.ui.theme.RubySurfaceCard
import com.example.ui.theme.TextWhite

@Composable
fun StudyvoTopBar(
    currentMode: StudyvoMode,
    onModeSelect: (StudyvoMode) -> Unit,
    onMenuClick: () -> Unit = {},
    onRightActionClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(RubyBackgroundDark)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left Menu Button (= icon in PDF)
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = TextWhite,
                modifier = Modifier.size(28.dp)
            )
        }

        // Center Interactive Title Dropdown ("Studyvo Flash v" or "Studyvo Exercises v")
        Box {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (currentMode == StudyvoMode.FLASH) "Studyvo Flash" else "Studyvo Exercises",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Ganti Mode",
                    tint = CoralAccent,
                    modifier = Modifier.size(24.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(RubySurfaceCard)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "⚡ Studyvo Flash (AI Chat)",
                            color = if (currentMode == StudyvoMode.FLASH) CoralAccent else TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    onClick = {
                        expanded = false
                        onModeSelect(StudyvoMode.FLASH)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "📝 Studyvo Exercises (Latihan Soal)",
                            color = if (currentMode == StudyvoMode.EXERCISES) CoralAccent else TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    onClick = {
                        expanded = false
                        onModeSelect(StudyvoMode.EXERCISES)
                    }
                )
            }
        }

        // Right Action Icon
        if (currentMode == StudyvoMode.FLASH) {
            IconButton(onClick = onRightActionClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Cari",
                    tint = TextWhite,
                    modifier = Modifier.size(26.dp)
                )
            }
        } else {
            IconButton(onClick = { onModeSelect(StudyvoMode.FLASH) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Keluar Latihan",
                    tint = TextWhite,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
