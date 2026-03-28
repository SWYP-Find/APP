package com.swyp4.team2

sealed class AppRoute(val route: String){
    object Splash : AppRoute("splash_screen")
    object Login : AppRoute("login_screen")
    object Onboarding : AppRoute("onboarding_screen")
    object Main : AppRoute("main_screen")
    object Scenario : AppRoute("scenario_screen")
    object Alarm : AppRoute("alarm_screen")
    object Setting : AppRoute("setting_screen")

    object DiscussionHistory : AppRoute("discussion_history_screen") // 마이-내 토론 기록
    object PhilosopherType : AppRoute("philosopher_type_screen")     // 마이-나는 어떤 철학자 일까?
    object ContentActivity : AppRoute("content_activity_screen")     // 마이-내 콘텐츠 활동
    object NoticeEvent : AppRoute("notice_event_screen")             // 마이-공지방 · 이벤트
    object SettingProfile : AppRoute("setting_profile_screen")       // 설정-프로필 편집
    object SettingAlarm : AppRoute("setting_alarm_screen")         // 설정-알림 설정

    object PreVote : AppRoute("pre_vote_screen")
    object PostVote : AppRoute("post_vote_screen")
    object Perspective : AppRoute("perspective_screen/{itemId}"){
        fun createRoute(itemId: Long): String {
            return "perspective_screen/$itemId"
        }
    }
    object PerspectiveDetail : AppRoute("perspective_detail_screen/{itemId}"){
        fun createRoute(itemId: Long): String {
            return "perspective_detail_screen/$itemId"
        }
    }

    object Curation : AppRoute("curation_screen")
    object PrivacyPolicy : AppRoute("privacy_policy_screen")
    object TermsOfService : AppRoute("terms_of_service_screen")
}