package com.picke.app

sealed class AppRoute(val route: String){
    object Splash : AppRoute("splash_screen")
    object Login : AppRoute("login_screen")
    object Onboarding : AppRoute("onboarding_screen")
    object Main : AppRoute("main_screen")
    object Alarm : AppRoute("alarm_screen")
    object Setting : AppRoute("setting_screen")

    object BattleRouting : AppRoute("battle_routing_screen/{battleId}") {
        fun createRoute(battleId: String) = "battle_routing_screen/$battleId"
    }

    object DiscussionHistory : AppRoute("discussion_history_screen") // 마이-내 토론 기록
    object PhilosopherType : AppRoute("philosopher_type_screen")     // 마이-나는 어떤 철학자 일까?
    object OtherPhilosopher : AppRoute("other_philosopher_screen/{reportId}") {
        fun createRoute(reportId: String): String {
            return "other_philosopher_screen/$reportId"
        }
    }
    object ContentActivity : AppRoute("content_activity_screen")     // 마이-내 콘텐츠 활동
    object NoticeEvent : AppRoute("notice_event_screen?noticeId={noticeId}") {  // 마이-공지방 · 이벤트
        fun createRoute() = "notice_event_screen"
        fun createRoute(noticeId: Long) = "notice_event_screen?noticeId=$noticeId"
    }
    object SettingProfile : AppRoute("setting_profile_screen")       // 설정-프로필 편집
    object SettingAlarm : AppRoute("setting_alarm_screen")         // 설정-알림 설정

    object PreVote : AppRoute("pre_vote_screen/{battleId}") {
        fun createRoute(battleId: String) = "pre_vote_screen/$battleId"
    }

    object Scenario : AppRoute("scenario_screen/{battleId}") {
        fun createRoute(battleId: String) = "scenario_screen/$battleId"
    }

    object PostVote : AppRoute("post_vote_screen/{battleId}") {
        fun createRoute(battleId: String) = "post_vote_screen/$battleId"
    }
    object Perspective : AppRoute("perspective_screen/{battleId}?commentId={commentId}"){
        fun createRoute(battleId: String): String {
            return "perspective_screen/$battleId"
        }
        fun createRoute(battleId: String, commentId: String): String {
            return "perspective_screen/$battleId?commentId=$commentId"
        }
    }
    object Comment : AppRoute("comment_screen/{itemId}?firstOptionId={firstOptionId}&commentId={commentId}"){
        fun createRoute(itemId: String): String = "comment_screen/$itemId"
        fun createRoute(itemId: String, firstOptionId: Long): String = "comment_screen/$itemId?firstOptionId=$firstOptionId"
        fun createRoute(itemId: String, commentId: String): String = "comment_screen/$itemId?commentId=$commentId"
    }

    object Recommend : AppRoute("recommend_screen/{battleId}"){
        fun createRoute(battleId: String): String {
            return "recommend_screen/$battleId"
        }
    }
    object PrivacyPolicy : AppRoute("privacy_policy_screen")
    object TermsOfService : AppRoute("terms_of_service_screen")
    object Withdraw : AppRoute("withdraw_screen")
    object Point : AppRoute("point_screen")
    object MakeBattle : AppRoute("makebattle_screen")

    object TodayBattle : AppRoute("tab_battle?battleId={battleId}") {
        fun createRoute(battleId: String) = "tab_battle?battleId=$battleId"
    }

    object NewUserOnboarding : AppRoute("new_user_onboarding_screen")
}