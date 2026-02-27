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

    // 랜덤 TIL 데이터 생성

    fun generateRandomTils() {
        val sampleData = listOf(
            Triple("Compose Navigation 학습", "Navigation 컴포넌트를 활용한 화면 전환을 배웠다", listOf("Compose", "Navigation")),
            Triple("Room DB 마이그레이션", "Room 데이터베이스 마이그레이션 전략을 학습했다", listOf("Room", "Database", "Migration")),
            Triple("Coroutine Flow 심화", "SharedFlow와 StateFlow의 차이를 이해했다", listOf("Coroutine", "Flow", "Kotlin")),
            Triple("Hilt 모듈 구성", "멀티모듈 프로젝트에서 Hilt 구성 방법을 익혔다", listOf("Hilt", "DI", "Multi-Module")),
            Triple("Material3 테마 커스텀", "다크모드 대응 색상 시스템을 구축했다", listOf("Material3", "Theme", "Design-System")),
        )

        val difficulties = Difficulty.entries
        val emotions = Emotion.entries

        viewModelScope.launch {
            sampleData.forEachIndexed { index, (title, learned, tags) ->
                val difficulty = difficulties[index % difficulties.size]
                val emotion = emotions[index % emotions.size]
                val emotionScore = (index % 5) + 1

                val now = java.time.LocalDate.now()
                val tilDate = now.withDayOfMonth((index * 3 + 1).coerceAtMost(now.lengthOfMonth()))
                val createdAt = tilDate.atStartOfDay(java.time.ZoneId.systemDefault())
                    .toInstant().toEpochMilli()

                val til = Til(
                    title = title,
                    learned = learned,
                    difficulty = "연습용 어려웠던 점",
                    tags = tags,
                    emotion = emotion,
                    emotionScore = emotionScore,
                    difficultyLevel = difficulty,
                    feedback = "잘 하고 있어요! $title 주제는 중요한 기초입니다. 🐾",
                    createdAt = createdAt,
                )
                saveTilUseCase(til)
            }
        }
    }
}
