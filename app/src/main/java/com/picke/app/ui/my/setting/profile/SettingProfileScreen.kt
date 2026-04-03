package com.picke.app.ui.my.setting.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.picke.app.R
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.theme.SwypTheme

@Composable
fun SettingProfileScreen(
    onBackClick: () -> Unit,
) {

    Scaffold(
        containerColor = SwypTheme.colors.surface,
        modifier = Modifier.systemBarsPadding(),
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.my_setting_profile),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = SwypTheme.colors.surface
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){

        }
    }
}