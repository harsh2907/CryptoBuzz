package com.example.cryptobuzz.presentation.pull_to_refresh

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class RefreshIndicatorState(val message:String) {
    Default("Up-to-date!"),
    PullingDown("Pull down…"),
    ReachedThreshold("Release to refresh!"),
    Refreshing("Refreshing…")
}


private const val maxHeight = 100

@Composable
fun PullToRefreshIndicator(
    indicatorState: RefreshIndicatorState,
    pullToRefreshProgress: Float,
    timeElapsed: String,
    textColor:Color = LocalContentColor.current,
    refreshIndicatorSize:Dp = 16.dp
) {
    val heightModifier = when (indicatorState) {
        RefreshIndicatorState.PullingDown -> {
            Modifier.height(
                (pullToRefreshProgress * 100)
                    .roundToInt()
                    .coerceAtMost(maxHeight).dp,
            )
        }
        RefreshIndicatorState.ReachedThreshold -> Modifier.height(maxHeight.dp)
        RefreshIndicatorState.Refreshing -> Modifier.wrapContentHeight()
        RefreshIndicatorState.Default -> Modifier.height(0.dp)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .then(heightModifier)
            .padding(15.dp),
        contentAlignment = Alignment.BottomStart,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = indicatorState.message,
                style = MaterialTheme.typography.labelLarge,
                color = textColor,
            )
            if (indicatorState == RefreshIndicatorState.Refreshing) {
                CircularProgressIndicator(
                    modifier = Modifier.size(refreshIndicatorSize),
                    color = textColor,
                    trackColor = Color.Gray,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = "Last update $timeElapsed",
                    style = MaterialTheme.typography.labelMedium,
                    color = textColor,
                )
            }
        }
    }
}