package com.swyp4.team2.ui.debate.model

enum class SpeakerType {
    NARRATOR, A, B
}

data class DebateScript(
    val scriptId: String,
    val startTimeMs: Long,
    val speakerType: SpeakerType,
    val speakerName: String,
    val text: String
) {
    // 🔥 화면에 그릴 때 SSML 태그(<speak>, <prosody> 등)를 제거하고 순수 텍스트만 반환하는 확장 프로퍼티
    val displayCleanText: String
        get() = text.replace(Regex("<.*?>"), "").trim()
}

val dummyDebateScripts = listOf(
    DebateScript(
        scriptId = "fdaac677-074f-4359-8230-890961b9b8f4",
        startTimeMs = 0,
        speakerType = SpeakerType.NARRATOR,
        speakerName = "나레이터",
        text = "<speak>재난이 일어났을 때, <break time='300ms'/> 사람들은 서로를 돕습니다. <break time='500ms'/> 재난이 길어지면, <break time='300ms'/> 사람들은 서로를 착취합니다. 어느 것이 인간의 본 모습일까요? 2,000년 넘게 철학자들이 싸워온 질문입니다.</speak>"
    ),
    DebateScript(
        scriptId = "c373aea8-3dc0-40dd-a06c-409c614e0ee9",
        startTimeMs = 14280,
        speakerType = SpeakerType.A,
        speakerName = "루소",
        text = "<speak><prosody rate='slow' pitch='+1st'>인간은 자연 상태에서 자유롭고 평화로웠습니다.</prosody> 경쟁도, 소유욕도, 증오도, 이것은 문명이 만든 것입니다. 아이를 보십시오. <emphasis level='strong'>태어난 아이는 나누고, 웃고, 타인의 고통에 울음으로 반응합니다.</emphasis> 악은 가르쳐지는 것입니다. 소유, 비교, 경쟁을 배우면서 인간은 타락합니다. 사회가 인간을 망쳤습니다.</speak>"
    ),
    DebateScript(
        scriptId = "f8f7fe52-e6b8-4e85-8794-7bc6edce7231",
        startTimeMs = 39288,
        speakerType = SpeakerType.B,
        speakerName = "홉스",
        text = "<speak><prosody rate='medium' pitch='-2st'>루소, 아름다운 이야기입니다만 현실을 보십시오.</prosody> 역사상 국가 없이 평화로웠던 사회는 없습니다. 제가 리바이어던에서 말했습니다. 자연 상태의 인간의 삶은 고독하고, 가난하고, 불결하고, 잔인하고, 짧다고. <emphasis level='strong'>국가와 법이 없다면 인간은 서로를 착취합니다.</emphasis> 문명은 타락이 아니라 구원입니다.</speak>"
    ),
    DebateScript(
        scriptId = "bd98fa12-13a6-4e64-bcb1-9a382f5239f2",
        startTimeMs = 61200,
        speakerType = SpeakerType.A,
        speakerName = "루소",
        text = "<speak>홉스, 당신이 말하는 폭력의 흔적. 그건 <emphasis level='moderate'>소유</emphasis>가 생긴 이후의 것입니다. 땅을 소유하고 자원을 두고 경쟁하면서 전쟁이 시작됐습니다. <prosody rate='slow'>최초의 악은 '이건 내 것이다'라고 말한 순간 시작됐습니다.</prosody> 그 전에 인간은 자연 속에서 필요한 만큼만 취했습니다.</speak>"
    ),
    DebateScript(
        scriptId = "0fc0f7be-8483-4824-bcbb-755d1e148eb9",
        startTimeMs = 80928,
        speakerType = SpeakerType.B,
        speakerName = "홉스",
        text = "<speak>루소, 그 소유욕은 어디서 왔습니까. 문명 이전의 인간에게도 영역이 있었고 먹이 경쟁이 있었습니다. 이기적 유전자는 진화의 산물입니다. <emphasis level='strong'>선함은 선택이고 교육이지, 기본값이 아닙니다.</emphasis> 인간이 선하게 행동하는 건 그렇게 하는 것이 이익이거나, 처벌이 두렵거나, 교육받았기 때문입니다.</speak>"
    ),
    DebateScript(
        scriptId = "78e59adb-ab78-474b-ab0b-482d71dc2efa",
        startTimeMs = 102288,
        speakerType = SpeakerType.A,
        speakerName = "루소",
        text = "<speak><prosody rate='slow' pitch='+1st'>그렇다면 왜 인간은 아무 이익 없이 낯선 이를 돕습니까.</prosody> 전쟁터에서 적을 살려주는 병사들, 재난 현장에서 자기 목숨을 거는 사람들. 이익 계산으로는 설명되지 않는 선함이 존재합니다. <emphasis level='strong'>그건 인간 내부에 있는 무언가입니다.</emphasis></speak>"
    ),
    DebateScript(
        scriptId = "63a2a3a4-82bd-43cc-86d8-edfa4e8b5b98",
        startTimeMs = 120552,
        speakerType = SpeakerType.B,
        speakerName = "홉스",
        text = "<speak><prosody pitch='-1st'>그런 선함은 감탄스럽습니다. 그러나 그것은 예외입니다.</prosody> 시스템은 예외가 아닌 평균으로 설계해야 합니다. 인간 대부분이 선하다고 가정한 사회는 그 소수의 악한 자에 의해 무너집니다. 법과 제도는 인간의 선함을 믿어서가 아니라, <emphasis level='strong'>믿지 않기 때문에</emphasis> 만들어진 것입니다. 성선설은 아름답지만 위험한 낭만입니다.</speak>"
    ),
    DebateScript(
        scriptId = "01b74fca-89bf-4ef1-bc06-3ea86f3235d8",
        startTimeMs = 141888,
        speakerType = SpeakerType.NARRATOR,
        speakerName = "나레이터",
        text = "<speak>루소는 말합니다. 악은 가르쳐지는 것이고, 인간의 기본값은 선이라고. <break time='400ms'/> 홉스는 말합니다. 선함은 제도가 만들어내는 것이고, 그것 없인 인간은 서로를 파괴한다고. <break time='600ms'/> <prosody rate='slow'>당신은 인간을 믿습니까?</prosody></speak>"
    )
)