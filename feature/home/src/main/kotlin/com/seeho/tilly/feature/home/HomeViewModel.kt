package com.seeho.tilly.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seeho.tilly.core.domain.AddCoinsUseCase
import com.seeho.tilly.core.domain.ClaimAttendanceUseCase
import com.seeho.tilly.core.domain.DeleteTilUseCase
import com.seeho.tilly.core.domain.GetAllTilsUseCase
import com.seeho.tilly.core.domain.GetShopItemsUseCase
import com.seeho.tilly.core.domain.SaveTilUseCase
import com.seeho.tilly.core.model.Difficulty
import com.seeho.tilly.core.model.Emotion
import com.seeho.tilly.core.model.ItemCategory
import com.seeho.tilly.core.model.RewardResult
import com.seeho.tilly.core.model.ShopItem
import com.seeho.tilly.core.model.Til
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.seeho.tilly.core.common.util.DateUtils
import java.time.LocalDate

@HiltViewModel
class HomeViewModel @Inject constructor(
    getAllTilsUseCase: GetAllTilsUseCase,
    private val deleteTilUseCase: DeleteTilUseCase,
    private val saveTilUseCase: SaveTilUseCase,
    private val claimAttendanceUseCase: ClaimAttendanceUseCase,
    private val addCoinsUseCase: AddCoinsUseCase,
    getShopItemsUseCase: GetShopItemsUseCase,
) : ViewModel() {

    // 코인 보상 이벤트
    private val _coinRewardEvent = MutableStateFlow<RewardResult?>(null)
    val coinRewardEvent: StateFlow<RewardResult?> = _coinRewardEvent.asStateFlow()

    init {
        // 홈 화면 진입 시 출석 보상 수령 시도 (5코인, 1일 1회)
        viewModelScope.launch {
            try {
                val rewardResult = claimAttendanceUseCase()
                if (rewardResult != null) {
                    _coinRewardEvent.value = rewardResult
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                // 출석 보상 실패해도 앱 정상 동작
            }
        }
    }

    /** 보상 다이얼로그 닫기 */
    fun consumeCoinRewardEvent() {
        _coinRewardEvent.value = null
    }

    /**
     * Home 화면 UI 상태
     * Flow를 StateFlow로 변환하여 Compose에서 수집
     * WhileSubscribed(5_000): 구독자가 없어도 5초간 캐싱 (화면 회전 등에 유리)
     */
    val uiState: StateFlow<HomeUiState> = getAllTilsUseCase()
        .map { tils ->
            if (tils.isEmpty()) {
                HomeUiState.Empty
            } else {
                HomeUiState.Success(tils)
            }
        }
        .catch { e ->
            emit(HomeUiState.Error(e.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading,
        )

    /** 오늘 작성한 TIL의 ID (없으면 null) — FAB 상태 전환에 사용 */
    val todayTilId: StateFlow<Long?> = getAllTilsUseCase()
        .map { tils ->
            val today = LocalDate.now()
            tils.firstOrNull { til ->
                DateUtils.timestampToLocalDate(til.createdAt) == today
            }?.id
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    // 장착 중인 아이템 (카테고리 → 아이템ID)
    val equippedItems: StateFlow<Map<ItemCategory, String>> = getShopItemsUseCase.getEquippedItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    // 전체 상점 아이템 (장착 아이템 ID → imageResName 변환용)
    val allShopItems: List<ShopItem> = getShopItemsUseCase.getAllItems()

    /** 삭제할 TIL ID (null이면 다이얼로그 미표시) */
    private val _deletingTilId = MutableStateFlow<Long?>(null)
    val deletingTilId: StateFlow<Long?> = _deletingTilId.asStateFlow()

    /** 삭제 다이얼로그 표시 */
    fun showDeleteDialog(id: Long) {
        _deletingTilId.value = id
    }

    /** 삭제 다이얼로그 숨기기 */
    fun dismissDeleteDialog() {
        _deletingTilId.value = null
    }

    /** 삭제 확정 */
    fun deleteTil() {
        val id = _deletingTilId.value ?: return
        viewModelScope.launch {
            try {
                deleteTilUseCase(id)
                dismissDeleteDialog()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                e.printStackTrace()
                dismissDeleteDialog()
            }
        }
    }

    // ========== 디버그용 ==========

    /** 디버그: 테스트 코인 5000 추가 */
    fun debugAddCoins(amount: Int = 5000) {
        viewModelScope.launch {
            addCoinsUseCase(amount)
        }
    }

    fun generateRandomTils() {
        // 시연용 데이터: 키워드 중복(도넛 차트 예쁘게), 감정-점수 일치
        data class SampleTil(
            val title: String,
            val learned: String,
            val difficulty: String,
            val tags: List<String>,
            val emotion: Emotion,
            val emotionScore: Int,
            val difficultyLevel: Difficulty,
            val feedback: String,
        )

        val samples = listOf(
            SampleTil(
                title = "Kotlin Coroutine 기초",
                learned = "launch와 async의 차이를 이해했다. structured concurrency 개념도 학습했다.",
                difficulty = "suspend 함수 내부에서 스레드 전환 시점이 헷갈렸다",
                tags = listOf("Kotlin", "Coroutine"),
                emotion = Emotion.NORMAL,
                emotionScore = 3,
                difficultyLevel = Difficulty.NORMAL,
                feedback = "Coroutine의 기초를 다져가고 있네요! 꾸준히 하면 금방 익숙해질 거예요 🐾",
            ),
            SampleTil(
                title = "Jetpack Compose State 관리",
                learned = "remember와 mutableStateOf의 차이를 이해했다. recomposition 조건도 파악했다.",
                difficulty = "derivedStateOf를 언제 써야 하는지 판단이 어려웠다",
                tags = listOf("Kotlin", "Compose", "State"),
                emotion = Emotion.SATISFACTION,
                emotionScore = 4,
                difficultyLevel = Difficulty.HARD,
                feedback = "State 관리의 핵심을 잘 짚었어요! Compose의 매력에 빠져가고 있군요 ✨",
            ),
            SampleTil(
                title = "Room Database 설계",
                learned = "Entity, DAO, Database 구조를 학습하고 1:N 관계 매핑도 구현해봤다.",
                difficulty = "마이그레이션 전략 수립이 복잡했다",
                tags = listOf("Android", "Room", "Database"),
                emotion = Emotion.HARD,
                emotionScore = 2,
                difficultyLevel = Difficulty.VERY_HARD,
                feedback = "DB 설계는 어려운 주제인데 잘 도전했어요. 한 걸음씩 나아가고 있어요 💪",
            ),
            SampleTil(
                title = "Hilt DI 멀티모듈 적용",
                learned = "멀티모듈에서 Hilt 모듈 구성 방법을 익히고 @InstallIn 스코프를 이해했다.",
                difficulty = "모듈 간 의존성 그래프가 복잡해서 빌드 에러가 많았다",
                tags = listOf("Kotlin", "Hilt", "Android"),
                emotion = Emotion.ACHIEVEMENT,
                emotionScore = 5,
                difficultyLevel = Difficulty.HARD,
                feedback = "멀티모듈 DI를 직접 구성하다니 대단해요! 정말 큰 성장이에요 🎉",
            ),
            SampleTil(
                title = "Compose Navigation 심화",
                learned = "Type-Safe Navigation과 중첩 NavGraph 구성을 학습했다.",
                difficulty = "SavedStateHandle에서 인자를 추출하는 패턴이 헷갈렸다",
                tags = listOf("Compose", "Navigation", "Android"),
                emotion = Emotion.SATISFACTION,
                emotionScore = 4,
                difficultyLevel = Difficulty.NORMAL,
                feedback = "Navigation 심화까지 도전하다니! 앱의 뼈대가 튼튼해지고 있어요 🏗️",
            ),
            SampleTil(
                title = "Flow와 StateFlow 비교",
                learned = "Cold Flow와 Hot Flow의 차이, SharedFlow vs StateFlow 사용 시점을 정리했다.",
                difficulty = "WhileSubscribed 전략의 타임아웃 값 설정 기준이 모호했다",
                tags = listOf("Kotlin", "Coroutine", "Flow"),
                emotion = Emotion.NORMAL,
                emotionScore = 3,
                difficultyLevel = Difficulty.NORMAL,
                feedback = "Flow의 핵심 개념을 잘 정리했네요. 비동기 처리 실력이 늘고 있어요 🌊",
            ),
        )

        viewModelScope.launch {
            // 오늘 기준 7일 전~2일 전 연속 배치 (2/20~2/25)
            val today = LocalDate.now()
            samples.forEachIndexed { index, sample ->
                val tilDate = today.minusDays((samples.size - index + 1).toLong())
                val createdAt = tilDate.atTime(20, 0)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toInstant().toEpochMilli()

                val til = Til(
                    title = sample.title,
                    learned = sample.learned,
                    difficulty = sample.difficulty,
                    tags = sample.tags,
                    emotion = sample.emotion,
                    emotionScore = sample.emotionScore,
                    difficultyLevel = sample.difficultyLevel,
                    feedback = sample.feedback,
                    createdAt = createdAt,
                )
                saveTilUseCase(til)
            }
        }
    }
}
