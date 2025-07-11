package com.example.newsapp

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Run a test with a [FlowTest] context. This will automatically finish all observers at the end of the test.
 * @param context The coroutine context to run the test in.
 * @param testBody The test body to run.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun runFlowTest(
    context: CoroutineContext = EmptyCoroutineContext,
    testBody: suspend FlowTest.() -> Unit
) = runTest(context = context) {
    val flowTest = FlowTest(this)
    testBody(flowTest)
    advanceUntilIdle()
    flowTest.observers.forEach { it.finish() }
}

data class FlowTest(
    val scope: TestScope,
    val observers: MutableList<TestFlowObserver<*>> = mutableListOf()
) {
    fun <T : Any> Flow<T>.test(): TestFlowObserver<T> {
        return TestFlowObserver(scope, scope.testScheduler, this).also {
            observers.add(it)
        }
    }
}
