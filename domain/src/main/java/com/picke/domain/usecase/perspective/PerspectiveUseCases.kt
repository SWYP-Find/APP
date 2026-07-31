package com.picke.domain.usecase.perspective

data class PerspectiveUseCases(
    val getMyPerspectiveUseCase: GetMyPerspectiveUseCase,
    val deletePerspectiveUseCase: DeletePerspectiveUseCase,
    val loadMainPerspectiveUseCase: LoadMainPerspectiveUseCase,
    val loadPerspectivesUseCase: LoadPerspectivesUseCase,
    val reportPerspectiveUseCase: ReportPerspectiveUseCase,
    val retryModerationUseCase: RetryModerationUseCase,
    val submitPerspectiveUseCase: SubmitPerspectiveUseCase,
    val togglePerspectiveLikeUseCase: TogglePerspectiveLikeUseCase
)