package com.picke.domain.usecase.comment

data class CommentUseCases(
    val loadCommentsUseCase: LoadCommentsUseCase,
    val deleteCommentUseCase: DeleteCommentUseCase,
    val reportCommentUseCase: ReportCommentUseCase,
    val submitCommentUseCase: SubmitCommentUseCase,
    val toggleCommentLikeUseCase: ToggleCommentLikeUseCase
)