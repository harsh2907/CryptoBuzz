package com.example.cryptobuzz.presentation.home.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cryptobuzz.R
import com.example.cryptobuzz.domain.model.CryptoCurrency
import com.example.cryptobuzz.presentation.home.components.CryptoListState
import com.example.cryptobuzz.presentation.pull_to_refresh.PullToRefreshLayout
import com.example.cryptobuzz.presentation.pull_to_refresh.PullToRefreshLayoutState
import com.example.cryptobuzz.presentation.utils.placeholder

@Composable
fun HomeScreen(
    pullToRefreshState: PullToRefreshLayoutState,
    cryptoListState: CryptoListState,
    onRefresh: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        PullToRefreshLayout(
            modifier = Modifier
                .fillMaxSize(),
            pullRefreshLayoutState = pullToRefreshState,
            onRefresh = onRefresh,
        ) {
            HomeScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                cryptoListState = cryptoListState,
                onRefresh = onRefresh
            )
        }
    }
}


@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    cryptoListState: CryptoListState,
    onRefresh: () -> Unit
) {

    Column(modifier = modifier) {

        AnimatedContent(
            targetState = cryptoListState,
            label = "home screen",
            transitionSpec = {
                (fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down))
                    .togetherWith(
                        fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
                    )
            }
        ) { state ->
            when {
                state.error.isNotEmpty() -> {
                    ErrorScreen(
                        errorMessage = state.error,
                        onClick = onRefresh
                    )
                }

                state.isLoading -> {
                    LoadingScreen()
                }

                else -> {
                    CryptoListScreen(cryptoList = state.cryptoList)
                }
            }
        }

    }
}

@Composable
fun CryptoListScreen(
    cryptoList: List<CryptoCurrency>
) {

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(cryptoList) { cryptoCurrency ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(cryptoCurrency.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "thumbnail",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(12.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = cryptoCurrency.fullName,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(12.dp)
                )

                Text(
                    text = "₹ ${cryptoCurrency.price}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            HorizontalDivider(
                Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                )
            )
        }
    }

}

@Composable
fun LoadingScreen(
    itemCount: Int = 6
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(itemCount) { _ ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(12.dp)
                        .placeholder(
                            isLoading = true,
                            showShimmerAnimation = true
                        )
                        .clip(ShapeDefaults.Medium)

                )

                Text(
                    text = "demo text for shimmer",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .placeholder(
                            isLoading = true,
                            showShimmerAnimation = true
                        )
                        .clip(ShapeDefaults.Medium)
                )

                Text(
                    text = "Demo",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .placeholder(
                            isLoading = true,
                            showShimmerAnimation = true
                        )
                        .clip(ShapeDefaults.Medium)
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(
    errorMessage: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.error_image),
            contentDescription = "Error Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(12.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "oooops",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Text(text = "Try Again")
        }
    }
}