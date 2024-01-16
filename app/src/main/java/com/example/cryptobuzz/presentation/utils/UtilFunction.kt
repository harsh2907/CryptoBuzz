package com.example.cryptobuzz.presentation.utils

import java.util.concurrent.TimeUnit

fun Double.format(digits: Int) = "%.${digits}f".format(this)


fun getLastUpdatedTimeString(lastUpdatedTimestamp: Long): String {
    val currentTime = System.currentTimeMillis()
    val durationInSeconds = TimeUnit.MILLISECONDS.toSeconds(currentTime - lastUpdatedTimestamp)

    return when {
        durationInSeconds < 60 -> "just now"
        durationInSeconds < 120 -> "a minute ago"
        durationInSeconds < 3600 -> "${TimeUnit.SECONDS.toMinutes(durationInSeconds)} minutes ago"
        durationInSeconds < 7200 -> "an hour ago"
        else -> "${TimeUnit.SECONDS.toHours(durationInSeconds)} hours ago"
    }
}


fun getTimePassedInMinSec(timePassedMs: Long): String {
    return when {
        timePassedMs < TimeUnit.MINUTES.toMillis(1) -> {
            TimeUnit.MILLISECONDS.toSeconds(timePassedMs).toString() + " seconds ago"
        }

        timePassedMs < TimeUnit.HOURS.toMillis(1) -> {
            TimeUnit.MILLISECONDS.toMinutes(timePassedMs).toString() + " minutes ago"
        }

        else -> ""
    }
}