package com.swyp4.team2.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp4.team2.domain.model.DebateMessage
import com.swyp4.team2.domain.model.SpeakerType

@Composable
fun ChatBubble(
    message: DebateMessage,
    isActive: Boolean
) {
    val isSpeakerA = message.speakerType == SpeakerType.A
    val alpha = if (isActive) 1f else 0.5f

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if(isSpeakerA) Arrangement.Start else Arrangement.End
    ){
        Column(
            modifier = Modifier.fillMaxWidth(0.8f)
        ){
            Text(
                text = message.speakerName,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp, start = 8.dp, end = 8.dp),

            )

            Box(
                modifier = Modifier.background(
                    color = if (isSpeakerA) Color(0xFFF5F5F5) else Color(0xFFE3F2FD).copy(alpha = alpha),
                    shape = RoundedCornerShape(16.dp)
                ).padding(16.dp)
            ){
                Text(
                    text = message.text,
                    fontSize = 16.sp,
                    color = Color.Black.copy(alpha = alpha),
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
