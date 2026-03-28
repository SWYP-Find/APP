package com.swyp4.team2.domain.model

data class PhilosopherReport(
    val hasTestResult: Boolean = false,
    val mainPhilosopher: MainPhilosopherDetail,
    val traitAnalysis: TraitAnalysis,
    val tasteReport: TasteReport,
    val chemistry: Chemistry
)

// 1. 내 철학자 상세 정보
data class MainPhilosopherDetail(
    val name: String,
    val description: String,
    val imageUrl: Any? = null,
    val tags: List<String> // 예: ["의무론", "규칙 중시", "보편 원칙", "이성적"]
)

// 2. 성향 분석 수치 (0~100점 기준)
data class TraitAnalysis(
    val principle: Int, // 원칙
    val logic: Int,     // 이성
    val individual: Int,// 개인
    val change: Int,    // 변화
    val inner: Int,     // 내면
    val ideal: Int      // 이상
)

// 3. 내 취향 리포트
data class TasteReport(
    val totalParticipation: Int, // 총 참여
    val opinionChanges: Int,     // 의견 전환
    val winRate: Int,            // 배틀 승률 (%)
    val topCategories: List<CategoryCount> // 순위별 해시태그 (최대 4개)
)

// 카테고리별 횟수
data class CategoryCount(
    val category: String, // 예: "철학"
    val count: Int        // 예: 20
)

// 4. 궁합 유형
data class Chemistry(
    val best: ChemistryPhilosopher,
    val worst: ChemistryPhilosopher
)

// 궁합 철학자 정보
data class ChemistryPhilosopher(
    val name: String,
    val description: String,
    val imageUrl: Any? = null
)
