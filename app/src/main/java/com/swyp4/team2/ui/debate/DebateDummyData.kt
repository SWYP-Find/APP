package com.swyp4.team2.ui.debate

import com.swyp4.team2.domain.model.DebateMessage
import com.swyp4.team2.domain.model.SpeakerType

object DebateDummyData {
    val debateScripts = listOf(
        DebateMessage(1, 0, SpeakerType.A, "윤리 전문가", "인간은 품위 있게 죽을 권리가 있습니다. 극심한 고통 속에서 삶을 연장하는 것이 정말 인간답다고 할 수 있을까요?"),
        DebateMessage(2, 7500, SpeakerType.B, "종교 지도자", "생명은 인간이 결정할 영역이 아닙니다. 고통조차 삶의 일부이며, 그 의미를 찾는 것이 우리의 과제입니다."),
        DebateMessage(3, 14200, SpeakerType.A, "윤리 전문가", "하지만 말기 환자 본인이 원한다면요? 자기결정권은 가장 기본적인 인권 아닌가요?"),
        DebateMessage(4, 21000, SpeakerType.B, "종교 지도자", "자기결정권을 존중해야 한다는 점은 동의합니다. 그러나 제도화되면 사회적 압력이 생기지 않을까요?"),
        DebateMessage(5, 28500, SpeakerType.A, "윤리 전문가", "남용의 위험은 엄격한 법적 절차와 전문 의료진의 심의로 충분히 방지할 수 있습니다. 이미 합법화된 국가들의 사례가 이를 증명하고 있죠."),
        DebateMessage(6, 36000, SpeakerType.B, "종교 지도자", "해외 사례를 보면 처음에는 말기 환자만 허용하다가, 점차 우울증 환자나 미성년자까지 적용 범위가 확대되는 '미끄러운 비탈길' 현상이 나타나고 있습니다. 이건 어떻게 보십니까?"),
        DebateMessage(7, 46500, SpeakerType.A, "윤리 전문가", "그건 제도 설계의 문제입니다. 명확한 가이드라인을 세우고 예외를 철저히 통제하는 시스템을 갖춘다면, 억지로 연명하며 겪는 비참한 고통을 줄이는 데 집중할 수 있습니다."),
        DebateMessage(8, 55000, SpeakerType.B, "종교 지도자", "인간의 가치는 건강 상태로 측정될 수 없습니다. 고통 속에서도 우리는 연대와 사랑을 배웁니다. 국가가 죽음을 돕는 대신 완화 의료(호스피스)를 전폭적으로 지원해야 합니다."),
        DebateMessage(9, 65000, SpeakerType.A, "윤리 전문가", "호스피스 치료도 한계가 명확합니다. 마약성 진통제로도 제어되지 않는 신체적 붕괴를 겪는 환자들에게 호스피스만 강요하는 것은 오히려 잔혹한 처사입니다."),
        DebateMessage(10, 74000, SpeakerType.B, "종교 지도자", "그렇다고 죽음을 처방하는 것이 해결책이 될 수는 없습니다. 이는 결국 치료비가 부담되는 사회적 약자들에게 '가족에게 짐이 되지 말고 죽음을 선택하라'는 무언의 압박으로 작용할 것입니다."),
        DebateMessage(11, 85000, SpeakerType.A, "윤리 전문가", "'죽음을 강요하는 것'이 아니라 '선택할 권리'를 돌려주는 것입니다. 자신의 마지막을 어떻게 장식할지 스스로 결정하는 것은 인간이 누릴 수 있는 가장 고귀한 자유입니다."),
        DebateMessage(12, 94000, SpeakerType.B, "종교 지도자", "자유라는 이름으로 생명의 절대적 가치를 훼손해서는 안 됩니다. 생명은 그 자체로 목적이지, 수단이나 선택의 대상이 결코 아닙니다.")
    )
}