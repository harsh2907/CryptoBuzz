package com.example.cryptobuzz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cryptobuzz.presentation.home.components.HomeViewModel
import com.example.cryptobuzz.presentation.home.screen.HomeScreen
import com.example.cryptobuzz.presentation.pull_to_refresh.RefreshIndicatorState
import com.example.cryptobuzz.presentation.pull_to_refresh.rememberPullToRefreshState
import com.example.cryptobuzz.presentation.utils.getTimePassedInMinSec
import com.example.cryptobuzz.ui.theme.CryptoBuzzTheme
import com.stevdzasan.messagebar.rememberMessageBarState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoBuzzTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    val messageBarState = rememberMessageBarState()
                    val homeViewModel: HomeViewModel = viewModel()
                    val cryptoState by homeViewModel.cryptoListState.collectAsStateWithLifecycle()

                    val pullToRefreshState = rememberPullToRefreshState(
                        onTimeUpdated = { timeElapsed ->
                            getTimePassedInMinSec(timeElapsed)
                        },
                    )

                    LaunchedEffect(key1 = cryptoState) {
                        if (cryptoState.error.isNotEmpty() || cryptoState.cryptoList.isNotEmpty()) {
                            pullToRefreshState.updateRefreshState(RefreshIndicatorState.Default)
                        }

                    }

                    HomeScreen(
                        pullToRefreshState = pullToRefreshState,
                        cryptoListState = cryptoState,
                        onRefresh = homeViewModel::fetchDataPeriodically
                    )
                }
            }
        }
    }
}
