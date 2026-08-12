package com.picke.presentation.ui.my.user.componenet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun CreditCard(
    credit: Int,
    onClick: () -> Unit,
    onChargeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.primaryDark)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color = PickeTheme.colors.secondary300, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "P",
                    style = PickeTheme.typography.b5Medium,
                    color = PickeTheme.colors.textSecondary
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.my_point),
                    style = PickeTheme.typography.b3Regular,
                    color = PickeTheme.colors.surfaceDefault
                )
                Text(
                    text = credit.toString(),
                    style = PickeTheme.typography.b3Regular,
                    color = PickeTheme.colors.secondary700
                )
            }
        }

        // [오른쪽] 무료 충전 버튼 (UI 비활성화)
        // Box(
        //     modifier = Modifier
        //         .clip(RoundedCornerShape(4.dp))
        //         .background(SwypTheme.colors.secondary300)
        //         .clickable { onChargeClick() }
        //         .padding(horizontal = 6.dp, vertical = 4.dp),
        //     contentAlignment = Alignment.Center
        // ) {
        //     Text(
        //         text = stringResource(R.string.my_charge_free),
        //         style = SwypTheme.typography.label,
        //         color = SwypTheme.colors.textPrimary
        //     )
        // }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditCardPreview() {
    PickeTheme {
        CreditCard(
            credit = 1,
            onClick = { },
            onChargeClick = { }
        )
    }
}