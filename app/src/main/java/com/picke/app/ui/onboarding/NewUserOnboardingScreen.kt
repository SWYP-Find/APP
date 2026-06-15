package com.picke.app.ui.onboarding

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.picke.app.ui.component.NotificationPermissionBottomSheet
import com.picke.app.ui.component.TermsOfServiceBottomSheet

private enum class OnboardingStep { TERMS, NOTIFICATION }

@Composable
fun NewUserOnboardingScreen(
    onComplete: () -> Unit,
    onViewServiceTerms: () -> Unit,
    onViewPrivacyPolicy: () -> Unit
) {
    var step by remember { mutableStateOf(OnboardingStep.TERMS) }
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { onComplete() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    )

    when (step) {
        OnboardingStep.TERMS -> {
            TermsOfServiceBottomSheet(
                onConfirm = { step = OnboardingStep.NOTIFICATION },
                onViewServiceTerms = onViewServiceTerms,
                onViewPrivacyPolicy = onViewPrivacyPolicy
            )
        }
        OnboardingStep.NOTIFICATION -> {
            NotificationPermissionBottomSheet(
                isDismissible = false,
                onDismiss = {},
                onAgree = {
                    markNotificationPermissionAsked(context)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        onComplete()
                    }
                },
                onDisagree = {
                    markNotificationPermissionAsked(context)
                    onComplete()
                }
            )
        }
    }
}

private fun markNotificationPermissionAsked(context: Context) {
    context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        .edit().putBoolean("notification_permission_asked", true).apply()
}
