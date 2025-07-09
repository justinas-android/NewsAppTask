package com.example.newsapp.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun <T : Any> T.toFlow(): Flow<T> = flowOf(this)
