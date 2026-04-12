package com.picke.app.ui.vote

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import com.picke.app.R
import com.picke.app.SwypApplication
import com.picke.app.domain.model.BattleDetailBoard
import com.picke.app.domain.model.BattleOptionBoard
import com.picke.app.ui.component.CustomButton
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.component.ProfileImage
import com.picke.app.ui.theme.*
import com.picke.app.util.shareBattleToInstagramStoryBrightMode
import com.picke.app.util.shareBattleToInstagramStoryDarkMode
import com.picke.app.util.shareBattleToKakao
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun VoteRoute(
    voteType: VoteType,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    viewModel: VoteViewModel = hiltViewModel()
) {
    BackHandler {
        onBackClick()
    }
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Primary900)
        }
    } else {
        uiState.battleDetail?.let { detail ->
            VoteScreen(
                voteType = voteType,
                battleDetail = detail,
                onBackClick = onBackClick,
                onVoteSubmit = onVoteSubmit,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun VoteScreen(
    voteType: VoteType,
    battleDetail: BattleDetailBoard,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    viewModel: VoteViewModel
) {
    val isPreVote = voteType == VoteType.PRE
    val battleInfo = battleDetail.battleInfo

    val bgColor = if (isPreVote) SwypTheme.colors.surface else Color.Black
    val titleColor = if (isPreVote) Gray900 else SwypTheme.colors.surface
    val descColor = if (isPreVote) Gray700 else Gray400

    var selectedOptionId by remember { mutableStateOf<String?>(null) }
    val isButtonEnabled = selectedOptionId != null

    val context = androidx.compose.ui.platform.LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    val graphicsLayer = androidx.compose.ui.graphics.rememberGraphicsLayer()
    var showShareDialog by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }

    val onKakaoShareClick = {
        isSharing = true
        coroutineScope.launch {
            try {
                val request = coil.request.ImageRequest.Builder(context)
                    .data(battleInfo.thumbnailUrl)
                    .allowHardware(false)
                    .build()
                val result = context.imageLoader.execute(request)
                val bitmap = (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap

                if (bitmap != null) {
                    shareBattleToKakao(
                        context = context,
                        bitmap = bitmap,
                        battleId = battleInfo.battleId,
                        battleTitle = battleInfo.title,
                        battleDescription = if (isPreVote) battleInfo.summary else battleDetail.description,
                        onComplete = { isSharing = false }
                    )
                } else {
                    isSharing = false
                    android.widget.Toast.makeText(context, "이미지 로드 실패", android.widget.Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                isSharing = false
                android.widget.Toast.makeText(context, "공유 실패", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    val onInstaShareClick = {
        isSharing = true
        coroutineScope.launch {
            try {
                kotlinx.coroutines.delay(100)

                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()

                if (isPreVote){
                    shareBattleToInstagramStoryBrightMode(
                        context = context,
                        bitmap = bitmap,
                        onComplete = { isSharing = false }
                    )
                } else{
                    shareBattleToInstagramStoryDarkMode(
                        context = context,
                        bitmap = bitmap,
                        onComplete = { isSharing = false }
                    )
                }

            } catch (e: Exception) {
                isSharing = false
                Toast.makeText(context, "캡처 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = bgColor,
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
                Box(modifier = Modifier.statusBarsPadding()) {
                    CustomTopAppBar(
                        centerTitle = false,
                        showBackButton = true,
                        onBackClick = onBackClick,
                        backIconColor = Color.White,
                        backgroundColor = Color.Transparent,
                        actions = {
                            Icon(
                                modifier = Modifier.size(20.dp)
                                    .clickable { showShareDialog = true },
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "공유",
                                tint = Color.White
                            )
                        }
                    )
                }
            },
            bottomBar = {
                Box(modifier = Modifier.navigationBarsPadding()) {
                    CustomButton(
                        text = if (isPreVote) stringResource(R.string.prevote) else "최종 투표하기",
                        onClick = {
                            if (selectedOptionId != null) {
                                viewModel.submitVote(
                                    voteType = voteType,
                                    selectedOptionId = selectedOptionId!!,
                                    onSuccess = {
                                        /*val props = JSONObject().apply {
                                        put("battle_id", battleInfo.battleId.toString())
                                        put("battle_title", battleInfo.title)
                                    }

                                    if (isPreVote) {
                                        SwypApplication.mixpanel.track("pre_vote", props) // 기획서 명칭 일치
                                    } else {
                                        SwypApplication.mixpanel.track("post_vote", props) // 기획서 명칭 일치
                                    }*/

                                        onVoteSubmit(battleInfo.battleId.toString())
                                    }
                                )
                            }
                        },
                        modifier = Modifier.padding(20.dp),
                        backgroundColor = if (isButtonEnabled) SwypTheme.colors.primary else Primary300,
                        textColor = Beige50
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(bgColor)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    SubcomposeAsyncImage(
                        model = battleInfo.thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Black.copy(alpha = 0.3f),
                                                Color.Transparent,
                                                bgColor
                                            ),
                                            startY = 0f,
                                            endY = size.height
                                        )
                                    )
                                }
                            },
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = SwypTheme.colors.primary,
                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            battleInfo.tags.forEach { tag ->
                                Surface(
                                    color = Color.White.copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(2.dp)
                                ) {
                                    Text(
                                        text = "#${tag.name}",
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 2.dp
                                        ),
                                        style = SwypTheme.typography.label,
                                        color = Primary500
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = battleInfo.title.replace(", ", ",\n"),
                            style = SwypTheme.typography.h1SemiBold,
                            color = titleColor
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isPreVote) battleInfo.summary else battleDetail.description,
                            style = SwypTheme.typography.b3Regular,
                            color = descColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (battleInfo.options.size >= 2) {
                            VoteOptionCard(
                                modifier = Modifier.weight(0.5f),
                                option = battleInfo.options[0],
                                isSelected = selectedOptionId == battleInfo.options[0].optionId,
                                onClick = { selectedOptionId = battleInfo.options[0].optionId }
                            )
                            VoteOptionCard(
                                modifier = Modifier.weight(0.5f),
                                option = battleInfo.options[1],
                                isSelected = selectedOptionId == battleInfo.options[1].optionId,
                                onClick = { selectedOptionId = battleInfo.options[1].optionId }
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = Color(0xFFF2E3C6)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("VS", style = SwypTheme.typography.labelMedium, color = Gray900)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (showShareDialog) {
            com.picke.app.ui.component.ShareDialog(
                onDismiss = { showShareDialog = false },
                onKakaoClick = {
                    showShareDialog = false
                    onKakaoShareClick()
                },
                onInstaClick = {
                    showShareDialog = false
                    onInstaShareClick()
                },
                onFacebookClick = {
                    showShareDialog = false
                },
                onCopyLinkClick = {
                    showShareDialog = false
                    viewModel.getShareLink(
                        battleId = battleInfo.battleId.toInt(),
                        onSuccess = { url ->
                            clipboardManager.setText(
                                androidx.compose.ui.text.AnnotatedString(
                                    url
                                )
                            )
                            android.widget.Toast.makeText(
                                context,
                                "링크가 클립보드에 복사되었습니다.",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = { errorMessage ->
                            android.widget.Toast.makeText(
                                context,
                                errorMessage,
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            )
        }

        if (isSharing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary900)
            }
        }
    }
}

@Composable
fun VoteOptionCard(
    modifier: Modifier = Modifier,
    option: BattleOptionBoard,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Secondary500 else Beige500
    val contentAlpha = if (isSelected) 1f else 0.8f

    Column(
        modifier = modifier
            .alpha(contentAlpha)
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, borderColor, RoundedCornerShape(2.dp))
            .background(Beige300)
            .clickable { onClick() }
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage(
            model = option.imageUrl,
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = option.title, style = SwypTheme.typography.h4SemiBold, color = Gray900, textAlign = TextAlign.Center )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = option.representative, style = SwypTheme.typography.labelXSmall, color = Gray500)
    }
}
