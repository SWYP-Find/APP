# Picke (픽케) Android

Picke는 논쟁적인 주제에 대해 서로 다른 "관점"을 나누고, 유저 간 의견 대결(배틀)·투표·댓글로 소통하는 소셜 디스커션 앱입니다. 
내레이션이 있는 시나리오 콘텐츠, 성향 진단("철학자 유형") 결과 공유, 출석체크 스트릭·포인트 보상 등 게이미피케이션 요소로 리텐션을 유도합니다.

- **패키지명**: `com.picke.app`
- **Gradle 루트 프로젝트명**: `SwypApp` (구팀명 SWYP 시절의 이름이 남아있음, GitHub 조직도 `SWYP-Find`)

## 주요 기능

| 기능 | 설명 |
|---|---|
| **배틀 (Battle)** | 특정 주제에 대한 의견 대결 콘텐츠. 투표(Vote)와 댓글(Comment)로 참여, "오늘의 배틀"(TodayBattle) 및 유저가 직접 배틀을 만드는 기능(마이페이지 내 배틀 만들기) 포함 |
| **관점 (Perspective)** | 주제에 대한 서로 다른 시각을 제시/탐색하는 콘텐츠 |
| **시나리오 (Scenario)** | 오디오 내레이션이 포함된 스토리형 콘텐츠. 재생 후 관점/배틀로 연결되는 흐름 |
| **철학자 유형 (Philosopher type)** | 성향 진단 결과를 제공하고 공유(딥링크 `/recap/`)할 수 있는 바이럴 기능 |
| **출석체크 (Attendance)** | 일일 출석 체크, 주간 스트릭 뱃지, 리워드 지급. 최근 가장 활발히 개발된 기능 |
| **폴/퀴즈 (PollQuiz)** | 배틀·시나리오와 연계된 투표/퀴즈 |
| **마이페이지** | 프로필, 포인트(인앱 재화), 공지, 알림 설정, 탈퇴, 활동 내역(토론/콘텐츠) 등 |
| **홈** | 베스트/신규/오늘의 픽/트렌딩 콘텐츠 큐레이션 |
| **탐색 (Explore)** | 콘텐츠 탐색 |
| **알림** | FCM 푸시 (배틀/관점/공지 알림), 딥링크 라우팅 |
| **로그인/공유** | 카카오·구글 소셜 로그인, 카카오톡·인스타그램 공유 |

## 프로젝트 아키텍처

단일 `:app` 모듈 구조이며 (멀티모듈 아님), **Clean Architecture (data / domain / presentation)** 3계층을 기능(feature) 단위로 나눈 구조입니다. Jetpack Compose 기반 UI, MVVM 패턴을 사용합니다.

```
app/src/main/java/com/picke/app/
├── SwypApplication.kt        # Hilt 진입점, Kakao SDK 초기화, FCM 채널 설정
├── MainActivity.kt
├── analytics/                 # Mixpanel 트래킹 헬퍼
├── di/                        # Hilt 모듈 (Network, Api, Repository, AdMob, Mixpanel)
├── data/
│   ├── local/                 # 로컬 저장소 (EncryptedSharedPreferences)
│   ├── model/                 # DTO
│   ├── remote/                 # Retrofit API 인터페이스 (기능별 1개씩), AuthInterceptor, TokenAuthenticator
│   └── repository/             # RepositoryImpl (기능별)
├── domain/
│   ├── model/                  # 도메인 모델. "XxxBoard" 네이밍 컨벤션 사용 (예: AttendanceBoard, BattleBoard)
│   ├── repository/             # Repository 인터페이스
│   └── usecase/                # 기능별 UseCase (최근 리팩터링됨: refactor/usecase-layer)
├── notification/               # FCMService
├── ui/                         # Compose 화면. 기능별 패키지, 각각 Screen + ViewModel + UiState (+ Skeleton) 구성
│   ├── attendance/, battleentry/, comment/, component/, explore/,
│   ├── home/, login/, main/, my/, onboarding/, perspective/,
│   ├── recommend/, scenario/, splash/, theme/, todaybattle/, vote/, alarm/
└── util/
```

**참고 사항**
- Room 의존성이 버전 카탈로그에는 있으나 실제 `app/build.gradle.kts`에서는 주석 처리되어 있습니다 — 현재 로컬 DB 미사용
- AdMob 연동 코드는 존재하지만 `SwypApplication.kt` 등에서 비활성화(주석 처리)된 상태

## 기술 스택

**언어 / 빌드**
- Kotlin 2.0.21 (JVM target 17)
- AGP 9.0.1, Gradle 9.1.0
- compileSdk 36 / minSdk 26 / targetSdk 36

**UI**
- Jetpack Compose (Compose BOM 2024.10.01), Material3 — XML 뷰 미사용
- Navigation Compose 2.8.5
- Coil 2.7.0 (이미지), Media3 ExoPlayer/UI 1.4.1 (시나리오 오디오 재생)

**아키텍처 / DI**
- Hilt 2.59.2 + KSP 2.0.21-1.0.28
- Clean Architecture (data/domain/ui), Paging3 3.3.2/3.4.2

**네트워킹**
- Retrofit 2.11.0 + Gson, OkHttp 4.12.0

**로컬 저장**
- androidx-security-crypto (EncryptedSharedPreferences) — Room/DataStore 미사용

**인증 / 소셜**
- Kakao SDK 2.20.1, Google Play Services Auth 21.0.0

**Firebase / 알림**
- Firebase BOM 33.5.1 (Realtime Database, Dynamic Links, FCM)

**분석 / 모니터링**
- Mixpanel 7.5.0, Sentry 5.8.0

**광고 (현재 비활성화)**
- Google Play Services Ads (AdMob) 23.6.0

**테스트**
- JUnit4, MockK 1.13.13, kotlinx-coroutines-test, Espresso, Compose UI Test

## 빌드 환경 설정

빌드 전 아래 파일에 값을 채워야 합니다 (둘 다 `.gitignore`에 포함되어 저장소에 없음):

**`local.properties`**에 필요한 키:
```
KAKAO_DEBUG_APPKEY=...
GOOGLE_WEB_CLIENT_ID=...
ADMOB_APP_ID=...
ADMOB_REWARDED_AD_UNIT_ID=...
MIXPANEL_PROJECT_TOKEN=...
SENTRY_DSN=...
# release 서명용
storeFile=...
storePassword=...
keyAlias=...
keyPassword=...
```

**`sentry.properties`**: ProGuard 매핑 업로드용 Sentry 인증 토큰 필요

**빌드 variant별 BASE_URL**
- `debug`: `https://dev.picke.store/`
- `release`: `https://picke.store/` (minify + shrink 활성화, 서명 필요)

## CI/CD

현재 GitHub Actions 등 CI 설정 없음.
