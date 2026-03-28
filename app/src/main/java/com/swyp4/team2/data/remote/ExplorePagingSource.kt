package com.swyp4.team2.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.swyp4.team2.domain.model.ContentCard
import retrofit2.HttpException
import java.io.IOException

fun ContentResponseDto.toDomain(): ContentCard {
    return ContentCard(
        id = this.id.toString(),
        title = this.title,
        description = this.description,
        optionA = this.optionA,
        optionB = this.optionB,
        timeLimit = this.timeLimit,
        participantCount = this.participantCount,
        hashtags = this.hashtags
    )
}

class ExplorePagingSource(
    private val api: ExploreApi,
    private val category: String,
    // 나중에 서버 API에 sort 파라미터가 생기면 여기에도 추가해서 넘겨주면 됩니다!
) : PagingSource<Int, ContentCard>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentCard> {
        return try {
            val page = params.key ?: 1

            // 1. 진짜 서버에 데이터 요청!
            val responseList = api.getContents(
                category = category,
                page = page,
                size = params.loadSize // PagingConfig에서 설정한 pageSize(10)가 들어옵니다
            )

            // 2. 서버에서 받은 DTO를 우리 화면용 ContentCard로 예쁘게 변환!
            val contentCards = responseList.map { it.toDomain() }

            LoadResult.Page(
                data = contentCards,
                prevKey = if (page == 1) null else page - 1,
                // 서버에서 내려준 리스트가 비어있으면(끝에 도달하면) 다음 페이지 호출을 멈춥니다!
                nextKey = if (contentCards.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            // 인터넷이 끊겼을 때의 에러 처리
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // 서버가 404, 500 등 에러 코드를 뱉었을 때의 처리
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ContentCard>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}