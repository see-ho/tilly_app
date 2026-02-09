package com.seeho.tilly.feature.tildetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seeho.tilly.core.designsystem.component.TillyTopAppBar
import com.seeho.tilly.core.designsystem.theme.TillyTheme
import com.seeho.tilly.core.model.Til
import com.seeho.tilly.core.navigation.TilDetail
import com.seeho.tilly.feature.tildetails.components.TilDetailCommentCard
import com.seeho.tilly.feature.tildetails.components.TilDetailHeader
import com.seeho.tilly.feature.tildetails.components.TilDetailSection

import com.seeho.tilly.core.model.mock.getSampleTil

@Composable
fun TilDetailScreen(
    tilDetail: TilDetail,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    // val uiState by viewModel.uiState.collectAsState()
    
    val til = getSampleTil(tilDetail.tilId)

    TilDetailContent(
        til = til,
        onBackClick = onBackClick,
        onDeleteClick = onDeleteClick
    )
}

@Composable
fun TilDetailContent(
    til: Til,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TillyTopAppBar(
                titleText = "TIL Details",
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            TilDetailHeader(til = til)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            TilDetailSection(
                title = "What I Learned",
                content = til.learned,
                icon = Icons.Default.Info
            )

            //TODO ICON 수정
            til.difficulty?.let {
                Spacer(modifier = Modifier.height(24.dp))
                TilDetailSection(
                    title = "What Was Difficult",
                    content = it,
                    icon = Icons.Default.Warning
                )
            }

            //TODO ICON 수정
            til.tomorrow?.let {
                Spacer(modifier = Modifier.height(24.dp))
                TilDetailSection(
                    title = "Tomorrow's Plan",
                    content = it,
                    icon = Icons.Default.Edit
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            TilDetailCommentCard(
                til = til,
                feedback = "정말 잘했는데요? 상태 관리를 공부했군요. 난이도가 어려운데 잘 해낸 것 같아요. 앞으로도 화이팅해봐요!"
            )
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TilDetailScreenPreview() {
    TillyTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            TilDetailContent(
                til = getSampleTil(12345L),
                onBackClick = {},
                onDeleteClick = {}
            )
        }
    }
}
