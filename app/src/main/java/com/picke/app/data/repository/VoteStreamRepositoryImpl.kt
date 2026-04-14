package com.picke.app.data.repository

import android.util.Log
import com.google.gson.Gson
import com.picke.app.BuildConfig
import com.picke.app.data.model.PollQuizVoteResponseDto
import com.picke.app.data.model.toDomainModel
import com.picke.app.domain.model.PollQuizVoteBoard
import com.picke.app.domain.repository.VoteStreamRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import javax.inject.Inject

class VoteStreamRepositoryImpl @Inject constructor(
    private val okHttpClient : OkHttpClient,
    private val gson: Gson
) : VoteStreamRepository {
    companion object {
        private const val TAG = "VoteStream_Picke"
    }

    override fun getVoteStatsStream(battleId: Long): Flow<PollQuizVoteBoard> = callbackFlow {
        Log.d(TAG, "[SSE] 배틀 $battleId 실시간 스트림 연결 시도")

        // 1. SSE 요청 생성
        val request = Request.Builder()
            .url("${BuildConfig.BASE_URL}api/v1/battles/$battleId/vote-stats/stream")
            .header("Accept", "text/event-stream")
            .build()

        // 2. 서버에서 이벤트가 수신될 때마다 동작할 리스너
        val listener = object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: okhttp3.Response) {
                Log.i(TAG, "[SSE] 파이프 연결 성공! (배틀: $battleId)")
            }

            override fun onEvent(eventSource: EventSource, id: String?, type: String?, data: String) {
                Log.d(TAG, "[SSE] 새 데이터 수신: $data")
                try {
                    // "timeout" 체크 (서버에서 연결 유지를 위해 빈 데이터를 보낼 때 무시)
                    if (!data.contains("\"timeout\"")) {
                        // JSON String -> DTO 파싱 -> Domain 모델 변환 후 Flow로 방출(emit)
                        val dto = gson.fromJson(data, PollQuizVoteResponseDto::class.java)
                        trySend(dto.toDomainModel())
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "[SSE] 데이터 파싱 에러: ${e.message}")
                }
            }

            override fun onClosed(eventSource: EventSource) {
                Log.w(TAG, "[SSE] 서버에 의해 연결 종료됨")
                close() // Flow 닫기
            }

            override fun onFailure(eventSource: EventSource, t: Throwable?, response: okhttp3.Response?) {
                Log.e(TAG, "[SSE] 통신 에러 발생: ${t?.message}")
                close(t) // 에러와 함께 Flow 닫기
            }
        }

        // 3. 팩토리를 통해 파이프(EventSource) 생성 및 실행
        val factory = EventSources.createFactory(okHttpClient)
        val eventSource = factory.newEventSource(request, listener)

        // 4. 코루틴 취소 시(페이지 이탈 시) 파이프 닫기
        awaitClose {
            Log.i(TAG, "[SSE] 화면 이탈로 인한 스트림 수동 종료 (cancel)")
            eventSource.cancel()
        }
    }
}