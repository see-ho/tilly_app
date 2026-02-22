package com.seeho.tilly.feature.mypage.coinhistory

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.designsystem.component.TillyTopAppBar
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.CoinTransaction
import com.seeho.tilly.core.model.CoinTransactionType
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CoinHistoryScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CoinHistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TillyTopAppBar(
                titleText = "코인 내역",
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        CoinHistoryContent(
            uiState = uiState,
            modifier = modifier.padding(innerPadding),
        )
    }
}

@Composable
fun CoinHistoryContent(
    uiState: CoinHistoryUiState,
    modifier: Modifier = Modifier,
) {
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        // 상단: 현재 보유 코인
        item {
            CoinBalanceHeader(
                balance = uiState.currentBalance,
                numberFormat = numberFormat,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
            )
        }

        // 날짜별 트랜잭션 리스트
        uiState.transactionsByDate.forEach { (date, transactions) ->
            // 날짜 헤더
            item(key = "date_$date") {
                DateHeader(
                    date = date,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
                )
            }

            // 해당 날짜의 트랜잭션 목록
            items(
                items = transactions,
                key = { it.id },
            ) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    numberFormat = numberFormat,
                )
            }
        }

        // 내역 없음
        if (!uiState.isLoading && uiState.transactionsByDate.isEmpty()) {
            item {
                Text(
                    text = "아직 코인 내역이 없어요",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                )
            }
        }

        // 하단 여백
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

/**
 * 상단 보유 코인 카드
 */
@Composable
private fun CoinBalanceHeader(
    balance: Int,
    numberFormat: NumberFormat,
    modifier: Modifier = Modifier,
) {
    TillyCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "현재 보유",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = 4.dp),
            ) {
                Text(
                    text = numberFormat.format(balance),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = " 코인",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }
    }
}

/**
 * 날짜 헤더
 */
@Composable
private fun DateHeader(
    date: LocalDate,
    modifier: Modifier = Modifier,
) {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    val dateText = when (date) {
        today -> "오늘"
        yesterday -> "어제"
        else -> date.format(DateTimeFormatter.ofPattern("M월 d일"))
    }

    Text(
        text = dateText,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(vertical = 4.dp),
    )
}

/**
 * 개별 트랜잭션 행
 */
@Composable
private fun TransactionItem(
    transaction: CoinTransaction,
    numberFormat: NumberFormat,
    modifier: Modifier = Modifier,
) {
    val isPositive = transaction.amount > 0
    // 획득은 초록, 사용은 빨강
    val amountColor = if (isPositive) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.error
    }
    val amountPrefix = if (isPositive) "+" else ""
    val timeText = transaction.createdAt.format(DateTimeFormatter.ofPattern("HH:mm"))

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 왼쪽: 금액 + 설명
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$amountPrefix${numberFormat.format(transaction.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = amountColor,
                )
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }

            // 오른쪽: 시간
            Text(
                text = timeText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CoinHistoryContentPreview() {
    val now = LocalDateTime.now()
    val yesterday = now.minusDays(1)

    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CoinHistoryContent(
                uiState = CoinHistoryUiState(
                    currentBalance = 1250,
                    transactionsByDate = sortedMapOf(
                        compareByDescending { it },
                        now.toLocalDate() to listOf(
                            CoinTransaction(
                                id = 1, amount = 20,
                                type = CoinTransactionType.TIL_REWARD,
                                description = "TIL 작성 보상",
                                balanceAfter = 1250,
                                createdAt = now,
                            ),
                            CoinTransaction(
                                id = 2, amount = -200,
                                type = CoinTransactionType.PURCHASE,
                                description = "책상 아이템 구매",
                                balanceAfter = 1230,
                                createdAt = now.minusHours(1),
                            ),
                        ),
                        yesterday.toLocalDate() to listOf(
                            CoinTransaction(
                                id = 3, amount = 20,
                                type = CoinTransactionType.TIL_REWARD,
                                description = "TIL 작성 보상",
                                balanceAfter = 1430,
                                createdAt = yesterday,
                            ),
                            CoinTransaction(
                                id = 4, amount = 30,
                                type = CoinTransactionType.STREAK_BONUS,
                                description = "7일 연속 학습 보너스",
                                balanceAfter = 1410,
                                createdAt = yesterday.minusMinutes(5),
                            ),
                        ),
                    ),
                    isLoading = false,
                ),
            )
        }
    }
}
