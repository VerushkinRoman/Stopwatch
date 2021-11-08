package com.posse.android.stopwatch.repository

interface TimestampProvider {
    fun getMilliseconds(): Long
}