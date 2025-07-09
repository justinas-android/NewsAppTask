package com.example.newsapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestFlowObserver<T>(
    scope: CoroutineScope,
    testScheduler: TestCoroutineScheduler,
    flow: Flow<T>
) {
    private val _values = mutableListOf<T>()
    val values: List<T>
        get() = _values
    private val job: Job = scope.launch(UnconfinedTestDispatcher(testScheduler)) {
        flow.collect { _values.add(it) }
    }

    fun finish() {
        job.cancel()
    }
}
