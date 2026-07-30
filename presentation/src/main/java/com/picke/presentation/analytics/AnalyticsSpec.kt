package com.picke.presentation.analytics

/**
 * Pické Mixpanel 이벤트 명세 (크로스플랫폼 계약서) 상수 정의
 *
 * - 이벤트명·속성키·enum 값은 모두 snake_case 소문자. iOS와 100% 일치해야 함.
 * - 값 변경/추가 시 명세 문서와 iOS에 동시 반영할 것.
 */
object AnalyticsEvent {
    // 핵심 퍼널
    const val SIGN_UP = "sign_up"
    const val BATTLE_STEP = "battle_step"
    const val COMMUNITY_ACTION = "community_action"
    const val REPORT_ACTION = "report_action"
    const val AD_REVENUE = "ad_revenue"
    const val ONBOARDING_STEP = "onboarding_step"
    const val POINT_ACTION = "point_action"
    const val NOTIFICATION_ACTION = "notification_action"
    const val SHARE_ACTION = "share_action"

    // 화면 / 상호작용
    const val SCREEN_VIEW = "screen_view"
    const val CONTENT_ACTION = "content_action"
    const val UI_ACTION = "ui_action"

    // 마이크로 (Tier 3, 초기 비활성 - 볼륨 큼, 양 팀 합의 후 켤 것)
    const val PLAYBACK_ACTION = "playback_action"
    const val ENGAGEMENT_ACTION = "engagement_action"
}

object AnalyticsProp {
    const val METHOD = "method"
    const val STEP_NAME = "step_name"
    const val CONTENT_ID = "content_id"
    const val CHOICE = "choice"
    const val IS_CHANGED = "is_changed"
    const val COMMENT_LENGTH = "comment_length"
    const val ACTION_TYPE = "action_type"
    const val TOP_INDICATOR = "top_indicator"
    const val PLACEMENT = "placement"
    const val STEP = "step"
    const val TYPE = "type"
    const val AMOUNT = "amount"
    const val BALANCE = "balance"
    const val ACTION = "action"
    const val UNREAD_COUNT = "unread_count"
    const val TARGET = "target"
    const val CHANNEL = "channel"
    const val SCREEN = "screen"
    const val REFERRER = "referrer"
    const val SECTION = "section"

    // 슈퍼 프로퍼티 (§3)
    const val OS_TYPE = "os_type"
    const val APP_VERSION = "app_version"
    const val BUILD = "build"
    const val IS_LOGGED_IN = "is_logged_in"
    const val LOGIN_PROVIDER = "login_provider"

    // 유저 프로퍼티 (§4)
    const val PROVIDER = "provider"
    const val SIGNUP_DATE = "signup_date"
    const val PHILOSOPHER_TYPE = "philosopher_type"
    const val POINT_BALANCE = "point_balance"
}

/** battle_step.step_name 허용값 */
object BattleStepName {
    const val PRE_VOTE = "pre_vote"
    const val AUDIO_END = "audio_end"
    const val POST_VOTE = "post_vote"
}

/** onboarding_step.step 허용값 */
object OnboardingStep {
    const val SPLASH = "splash"
    const val LOGIN_SHOWN = "login_shown"
    const val KAKAO_START = "kakao_start"
    const val TERMS_SHOWN = "terms_shown"
    const val TERMS_AGREED = "terms_agreed"
    const val PERMISSION_ASKED = "permission_asked"
    const val HOME_ENTERED = "home_entered"
}

/** point_action.type 허용값 */
object PointActionType {
    const val ATTENDANCE_EARN = "attendance_earn"
    const val AD_EARN = "ad_earn"
    const val BATTLE_SPEND = "battle_spend"
}

/** notification_action.action 허용값 */
object NotificationActionType {
    const val VIEW_LIST = "view_list"
    const val READ_ALL = "read_all"
    const val ITEM_TAP = "item_tap"
}

/** share_action.target 허용값 (모든 공유는 share_action으로 통일, recap 리포트 공유 포함) */
object ShareTarget {
    const val RECAP = "recap"
    const val BATTLE = "battle"
    const val FINAL_VOTE = "final_vote"
}

/** share_action.channel 값 (계약서에 enum 미고정 - iOS와 동일 문자열 사용) */
object ShareChannel {
    const val KAKAO = "kakao"
    const val INSTAGRAM = "instagram"
    const val FACEBOOK = "facebook"
    const val LINK = "link"
}

/** report_action.action_type 허용값 (조회 전용, 공유는 share_action 사용) */
object ReportActionType {
    const val VIEW = "view"
}

/** content_action.action 허용값 */
object ContentActionType {
    const val BATTLE_CARD_TAP = "battle_card_tap"
    const val HERO_TAP = "hero_tap"
    const val NEW_BATTLE_TAP = "new_battle_tap"
    const val VOTE_CARD_TAP = "vote_card_tap"
    const val VOTE_RESULT_VIEW = "vote_result_view"
    const val QUICK_BATTLE_NEXT = "quick_battle_next"
    const val EXPLORE_CATEGORY_TAP = "explore_category_tap"
    const val BATTLE_RECOMMEND_CLOSE = "battle_recommend_close"
}

/** content_action.section 허용값 */
object ContentSection {
    const val BEST = "best"
    const val HOT = "hot"
    const val NEW = "new"
    const val VOTE = "vote"
}

/** ui_action.action 버튼 식별자 (§2.1 - {screen}_{button} 규약) */
object UiActionName {
    const val TAB_HOME = "tab_home"
    const val TAB_EXPLORE = "tab_explore"
    const val TAB_QUICK_BATTLE = "tab_quick_battle"
    const val TAB_MYPAGE = "tab_mypage"
    const val SETTINGS_LOGOUT = "settings_logout"
    const val SETTINGS_WITHDRAW = "settings_withdraw"
    const val NOTIFICATION_READ_ALL = "notification_read_all"
    const val RECAP_SHARE = "recap_share"
}

/**
 * screen_view.screen enum (§2) 및 NavController route → screen 매핑
 *
 * 신규 화면 추가 시 여기에 상수 등록 후 fromRoute()에 매핑 추가 (iOS 동시 반영).
 */
object AnalyticsScreen {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val QUICK_BATTLE = "quick_battle"
    const val MYPAGE = "mypage"
    const val BATTLE_DETAIL = "battle_detail"
    const val PREVOTE = "prevote"
    const val CHATROOM = "chatroom"
    const val CURATION = "curation"
    const val VOTE_CONTENT = "vote_content"
    const val COMMENT = "comment"
    const val COMMENT_REPLY = "comment_reply"
    const val NOTIFICATION = "notification"
    const val POINT = "point"
    const val SETTINGS = "settings"
    const val WITHDRAW = "withdraw"
    const val RECAP = "recap"

    /**
     * NavController route → §2 screen enum 매핑.
     * 매핑에 없는 route(마이 하위 화면 등 명세 미등재 화면)는 null 반환 → screen_view 미전송.
     */
    fun fromRoute(route: String): String? = when (route) {
        "onboarding_screen" -> ONBOARDING
        "login_screen" -> LOGIN
        "tab_home" -> HOME
        "tab_explore" -> EXPLORE
        "tab_battle", "tab_battle?battleId={battleId}" -> QUICK_BATTLE
        "tab_my" -> MYPAGE
        "pre_vote_screen/{battleId}" -> PREVOTE
        "scenario_screen/{battleId}" -> CHATROOM
        "post_vote_screen/{battleId}" -> VOTE_CONTENT
        "perspective_screen/{battleId}?commentId={commentId}" -> BATTLE_DETAIL
        "comment_screen/{itemId}?firstOptionId={firstOptionId}&commentId={commentId}" -> COMMENT
        "recommend_screen/{battleId}" -> CURATION
        "alarm_screen" -> NOTIFICATION
        "point_screen" -> POINT
        "setting_screen" -> SETTINGS
        "withdraw_screen" -> WITHDRAW
        "philosopher_type_screen" -> RECAP
        "other_philosopher_screen/{reportId}" -> RECAP
        else -> null
    }
}