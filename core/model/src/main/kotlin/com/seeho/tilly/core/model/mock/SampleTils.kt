package com.seeho.tilly.core.model.mock

import com.seeho.tilly.core.model.Til

val sampleTils = listOf(
    Til(
        id = 12345L,
        title = "Jetpack Compose ViewModel로 상태 관리하기",
        learned = "오늘은 Jetpack Compose에서 ViewModel과 StateFlow를 사용해서 상태를 제대로 관리하는 방법을 배웠다.\n\nConfiguration 변경에도 상태가 유지되는 간단한 카운터 앱을 구현했다.\n핵심은 collectAsStateWithLifecycle()을 사용해서 Compose UI에서 StateFlow를 관찰하는 것이었다.\n\nremember vs rememberSaveable의 차이도 배웠는데, 후자는 프로세스 종료에도 살아남는다.",
        difficulty = "MutableStateFlow와 MutableState를 언제 써야 하는지 헷갈렸다.\nStateFlow가 ViewModel에 더 적합한데, 라이프사이클 인식이 가능하고 코루틴과 잘 작동하기 때문이다.\n\n리컴포지션 문제도 있었는데, remember{}를 제대로 써서 불필요한 리컴포지션을 피해야 했다.",
        tomorrow = "내일은 사용자 입력 폼이 있는 더 복잡한 예제를 구현할 계획이다.\n계산된 상태를 위한 derivedStateOf도 알아볼 예정이다.\n람다 캡처 처리를 위한 rememberUpdatedState도 공부하고 싶다.",
        tags = listOf("jetpack compose", "viewmodel", "kotlin"),
        emotionScore = 4,
        difficultyLevel = "HARD",
        createdAt = 1770597018045L
    ),
    Til(
        id = 2L,
        title = "Hilt와 의존성 주입",
        learned = "Hilt를 사용해서 안드로이드 앱에서 의존성 주입을 자동화하는 방법을 학습했습니다.",
        difficulty = "어노테이션이 많아서 처음에는 조금 혼란스러웠습니다.",
        tomorrow = "커스텀 스코프에 대해 더 알아볼 예정입니다.",
        tags = listOf("hilt", "di", "android"),
        emotionScore = 5,
        difficultyLevel = "NORMAL",
        createdAt = 1770510618045L
    )
)

/**
 * SampleTil 하나 가져오기
 */
fun getSampleTil(id: Long): Til {
    return sampleTils.find { it.id == id } ?: sampleTils.first()
}
