package com.example.cryptobuzz.presentation.utils

import java.util.concurrent.TimeUnit

fun Double.format(digits: Int) = "%.${digits}f".format(this)

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