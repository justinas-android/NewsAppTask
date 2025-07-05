package com.example.newsapp.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

open class Interactor {
    fun <T> wrapCoroutineFlowResult(
        coroutine: suspend () -> T
    ): Flow<BaseResult<T>> = try {
        flow { emit(coroutine()) }
            .map { BaseResult.Success(it) }
            .catch<BaseResult<T>> { emit(it.toBaseResult()) }
    } catch (exception: Throwable) {
        flow { emit(exception.toBaseResult()) }
    }.map { it }

    private suspend fun <T> Throwable.toBaseResult(): BaseResult<T> {
        return BaseResult.Error(this.getMessage())
    }

    open suspend fun Throwable.getMessage(): String {
//        crashlytics.logException(this)
        return message ?: "Something went wrong"
    }
}

sealed class BaseResult<out Type> {
    data class Success<out Type>(val data: Type) : BaseResult<Type>()
    data class Error(val message: String) : BaseResult<Nothing>()
}

val <T> BaseResult<T>.data: T? get() = (this as? BaseResult.Success)?.data

val <T> BaseResult<T>.error: String? get() = (this as? BaseResult.Error)?.message

suspend fun <T> Flow<BaseResult<T>>.collectLatestResult(
    onError: suspend (BaseResult.Error) -> Unit = { },
    onSuccess: suspend (T) -> Unit = {}
) = collectLatest { result ->
    when (result) {
        is BaseResult.Success -> {
            onSuccess(result.data)
        }
        is BaseResult.Error -> {
            onError.invoke(result)
        }
    }
}
