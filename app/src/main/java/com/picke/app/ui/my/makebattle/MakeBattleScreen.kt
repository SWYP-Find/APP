package com.picke.app.ui.my.makebattle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.picke.app.ui.component.CustomButton
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Beige600
import com.picke.app.ui.theme.Gray200
import com.picke.app.ui.theme.Gray300
import com.picke.app.ui.theme.Gray400
import com.picke.app.ui.theme.Gray900
import com.picke.app.ui.theme.Primary300
import com.picke.app.ui.theme.Primary500
import com.picke.app.ui.theme.Primary800
import com.picke.app.ui.theme.SwypAppTheme
import com.picke.app.ui.theme.SwypTheme

@Composable
fun MakeBattleScreen(
    modifier: Modifier = Modifier,
    onBackClick : ()->Unit,
) {
    val categories = listOf("철학", "문학", "예술", "과학", "사회", "역사")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    var topic by remember { mutableStateOf("") }
    var stanceA by remember { mutableStateOf("") }
    var stanceB by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val maxDescLength = 200
    val isFormValid = topic.isNotBlank() && stanceA.isNotBlank() && stanceB.isNotBlank()

    Scaffold(
        containerColor = Beige200,
        contentWindowInsets = WindowInsets(0.dp),
        topBar={
            CustomTopAppBar(
                title = "배틀 만들기",
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = Beige200,
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .background(Beige200)
                    //.navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                CustomButton(
                    text = "제안하기 (-30P)",
                    onClick = {
                        if (isFormValid) {
                            // TODO: 제출 API 연동
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    backgroundColor = if (isFormValid) Primary500 else Primary300,
                    textColor = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ){
            Spacer(modifier = Modifier.height(24.dp))

            // 1. 카테고리 설정
            SectionTitle(title = "카테고리", isRequired = true)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(Color.White)
            ){
               categories.forEach { category ->
                   CategoryTab(
                       text = category,
                       isSelected = selectedCategory == category,
                       modifier = Modifier.weight(1f),
                       onClick = { selectedCategory = category }
                   )
               }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // 2. 주제 입력
            SectionTitle(title = "주제", isRequired = true)
            Spacer(modifier = Modifier.height(8.dp))
            CustomFormTextField(
                value = topic,
                onValueChange = { topic = it },
                placeholder = "논쟁이 될만한 주제를 한 줄로 써주세요",
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))


            // 3. 양측 입장 입력
            SectionTitle(title = "양측 입장", isRequired = true)
            Spacer(modifier = Modifier.height(8.dp))
            StanceInputField(
                label = "A",
                labelColor = Primary500,
                value = stanceA,
                onValueChange = { stanceA = it },
                placeholder = "첫 번째 입장을 입력하세요",
                isTop = true
            )
            StanceInputField(
                label = "B",
                labelColor = Gray900,
                value = stanceB,
                onValueChange = { stanceB = it },
                placeholder = "두 번째 입장을 입력하세요",
                isTop = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. 부가 설명 입력
            SectionTitle(title = "부가 설명", isRequired = false)
            Spacer(modifier = Modifier.height(8.dp))
            CustomFormTextField(
                value = description,
                onValueChange = {
                    if (it.length <= maxDescLength) {
                        description = it
                    }
                },
                placeholder = "이 주제를 제안하는 이유나 배경을 자유롭게 써주세요",
                singleLine = false,
                modifier = Modifier.height(92.dp),
                bottomRightText = "${description.length}/$maxDescLength"
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String, isRequired: Boolean) {
    val title = if (isRequired) "$title *" else "$title (선택)"

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            color = Gray400,
            style = SwypTheme.typography.labelMedium
        )
    }
}

@Composable
fun CategoryTab(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) Primary500 else Color.White
    val textColor = if (isSelected) Color.White else Gray300

    Box(
        modifier = modifier
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = if (isSelected) SwypTheme.typography.b3SemiBold else SwypTheme.typography.b3Regular
        )
    }
}

@Composable
fun CustomFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean,
    modifier: Modifier = Modifier,
    bottomRightText: String? = null,
    leadingContent: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Beige600)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (leadingContent != null) {
                leadingContent()
            }

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = singleLine,
                    textStyle = SwypTheme.typography.b4Medium,
                    modifier = Modifier.fillMaxWidth()
                ) { innerTextField ->
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = Gray200, style = SwypTheme.typography.b4Medium)
                    }
                    innerTextField()
                }
            }
        }

        if (bottomRightText != null) {
            Text(
                text = bottomRightText,
                color = Gray400,
                style = SwypTheme.typography.labelXSmall,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}

@Composable
fun StanceInputField(
    label: String,
    labelColor: Color,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isTop: Boolean
) {
    CustomFormTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        singleLine = true,
        leadingContent = {
            Text(
                text = label,
                color = labelColor,
                style = SwypTheme.typography.b3SemiBold,
                modifier = Modifier.width(28.dp)
            )
        }
    )
}