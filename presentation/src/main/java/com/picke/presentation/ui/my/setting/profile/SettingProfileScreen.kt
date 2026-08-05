package com.picke.presentation.ui.my.setting.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun SettingProfileScreen(
    onBackClick: () -> Unit,
) {

    Scaffold(
        containerColor = PickeTheme.colors.surface,
        modifier = Modifier.systemBarsPadding(),
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.my_setting_profile),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = PickeTheme.colors.surface
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){

        }
    }
}