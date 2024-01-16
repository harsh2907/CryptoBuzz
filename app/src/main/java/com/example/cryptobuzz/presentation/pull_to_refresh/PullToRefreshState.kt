package com.example.cryptobuzz.presentation.pull_to_refresh

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullToRefreshLayout(
    modifier: Modifier = Modifier,
    pullRefreshLayoutState: PullToRefreshLayoutState,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
) {
    val refreshIndicatorState by pullRefreshLayoutState.refreshIndicatorState
    val timeElapsedSinceLastRefresh by pullRefreshLayoutState.lastRefreshText

    val pullToRefreshState = rememberPullRefreshState(
        refreshing = refreshIndicatorState == RefreshIndicatorState.Refreshing,
        refreshThreshold = 120.dp,
        onRefresh = {
            onRefresh()
            pullRefreshLayoutState.refresh()
        },
    )

    LaunchedEffect(key1 = pullToRefreshState.progress) {
        when {
            pullToRefreshState.progress >= 1 -> {
                pullRefreshLayoutState.updateRefreshState(RefreshIndicatorState.ReachedThreshold)
            }

            pullToRefreshState.progress > 0 -> {
                pullRefreshLayoutState.updateRefreshState(RefreshIndicatorState.PullingDown)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullToRefreshState),
    ) {
        PullToRefreshIndicator(
            indicatorState = refreshIndicatorState,
            pullToRefreshProgress = pullToRefreshState.progress,
            timeElapsed = timeElapsedSinceLastRefresh,
        )
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}


class PullToRefreshLayoutState(
    val onTimeUpdated: (Long) -> String,
) {

    private val _lastRefreshTime: MutableStateFlow<Long> = MutableStateFlow(System.currentTimeMillis())

    var refreshIndicatorState = mutableStateOf(RefreshIndicatorState.Default)
        private set

    var lastRefreshText = mutableStateOf("")
        private set

    fun updateRefreshState(refreshState: RefreshIndicatorState) {
        val now = System.currentTimeMillis()
        val timeElapsed = now - _lastRefreshTime.value
        lastRefreshText.value = onTimeUpdated(timeElapsed)
        refreshIndicatorState.value = refreshState
    }

    fun refresh() {
        _lastRefreshTime.value = System.currentTimeMillis()
        updateRefreshState(RefreshIndicatorState.Refreshing)
    }
}

@Composable
fun rememberPullToRefreshState(
    onTimeUpdated: (Long) -> String,
): PullToRefreshLayoutState =
    remember {
        PullToRefreshLayoutState(onTimeUpdated)
    }