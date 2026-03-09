package com.swyp4.team2.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToMain: ()->Unit
) {
    val nickname by viewModel.nickname.collectAsState()
    val selectedCharacterId by viewModel.selectedCharacterId.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp),
    ){
        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(8) { index->
                val characterId = index + 1
                val isSelected = selectedCharacterId == characterId

                Box(
                    modifier = Modifier.aspectRatio(1f)
                        .background(
                            color = if(isSelected) Color.LightGray else Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = if(isSelected) 2.dp else 1.dp,
                            color = if(isSelected) Color.Black else Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable{ viewModel.selectCharacter(characterId) },
                    contentAlignment = Alignment.Center,
                ){
                    Text(
                        text = "🦊",
                        fontSize = 32.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.submitProfile(onSuccess = onNavigateToMain)
            },
            modifier = Modifier.fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                disabledContainerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = selectedCharacterId != null
        ){
            Text("시작하기", color = Color.White, fontSize = 16.sp)
        }

    }
}