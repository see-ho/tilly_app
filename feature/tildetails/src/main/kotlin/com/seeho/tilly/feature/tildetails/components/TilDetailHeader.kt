package com.seeho.tilly.feature.tildetails.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.component.TillyCard
import com.seeho.tilly.core.designsystem.component.TillyTag
import com.seeho.tilly.core.model.Til
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.common.util.DateUtils

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TilDetailHeader(
    til: Til,
    modifier: Modifier = Modifier,
) {
    val formattedDate = remember(til.createdAt) { DateUtils.formatToDotDateTime(til.createdAt) }

    TillyCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Text(
                text = til.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (til.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    til.tags.forEach { tag ->
                        TillyTag(text = tag)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TilDetailHeaderPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TilDetailHeader(
                til = Til(
                    title = "Jetpack Compose ViewModel로 상태 관리하기",
                    createdAt = System.currentTimeMillis(),
                    tags = listOf("jetpack compose", "viewmodel", "kotlin"),
                    learned = ""
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
