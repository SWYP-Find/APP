package com.picke.presentation.ui.perspective.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun PerspectiveInputField(
    textFieldState: TextFieldState,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    hintText: String = "의견을 남겨보세요...",
    editingKey: Any? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(editingKey) {
        if (editingKey != null) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Surface(
        color = PickeTheme.colors.surfaceTertiary,
        shadowElevation = 16.dp,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // 텍스트 입력 영역
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (isEnabled) PickeTheme.colors.surface else PickeTheme.colors.beige100,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                // Hint
                if (textFieldState.text.isEmpty()) {
                    Text(
                        text = hintText,
                        style = PickeTheme.typography.b3Regular,
                        color = PickeTheme.colors.outline,
                        lineHeight = 20.sp
                    )
                }

                BasicTextField(
                    state = textFieldState,
                    enabled = isEnabled,
                    lineLimits = TextFieldLineLimits.MultiLine(
                        minHeightInLines = 3,
                        maxHeightInLines = Int.MAX_VALUE
                    ),
                    textStyle = PickeTheme.typography.b3Regular.copy(
                        color = PickeTheme.colors.textPrimary,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 보내기 버튼
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isEnabled) PickeTheme.colors.buttonPrimaryBackground else PickeTheme.colors.buttonPrimaryBackgroundDisabled)
                    .clickable(enabled = isEnabled) { onSubmit() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "등록",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerspectiveInputFieldPreview() {
    val inputFieldState = rememberTextFieldState()

    PickeTheme {
        PerspectiveInputField(
            textFieldState = inputFieldState,
            onSubmit = {}
        )
    }
}