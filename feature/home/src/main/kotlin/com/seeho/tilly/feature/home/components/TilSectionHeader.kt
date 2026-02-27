package com.seeho.tilly.feature.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.seeho.tilly.core.designsystem.theme.TillyTheme

@Composable
fun TilSectionHeader(
    modifier: Modifier = Modifier,
    isListView: Boolean = true,
    onViewChange: (Boolean) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "TIL",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        //TODO List 보기, Calendar 보기
//        Row {
//            IconButton(onClick = { onViewChange(true) }) {
//                Icon(
//                    imageVector = Icons.Default.List,
//                    contentDescription = "List View",
//                    tint = if (isListView) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
//                )
//            }
//            IconButton(onClick = { onViewChange(false) }) {
//                Icon(
//                    imageVector = Icons.Default.DateRange,
//                    contentDescription = "Calendar View",
//                    tint = if (!isListView) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
//                )
//            }
//        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TilSectionHeaderPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column {
                TilSectionHeader(isListView = true)
                TilSectionHeader(isListView = false)
            }
        }
    }
}
