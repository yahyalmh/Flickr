package com.example.ui.common.ext

import com.example.ui.common.connectivity.ConnectivityMonitor
import kotlinx.coroutines.*
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.flow.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext

fun <T> Flow<T>.retryWithPolicy(
    retryPolicy: RetryPolicy = RetryPolicy.DefaultRetryPolicy,
    retryHandler: (e: Throwable) -> Unit
): Flow<T> {
    var currentDelay = retryPolicy.delayMillis

    return retryWhen { cause, attempt ->
        retryHandler(cause)
        if (cause is IOException && attempt < retryPolicy.numRetries) {
            delay(currentDelay)
            currentDelay *= retryPolicy.delayFactor
            return@retryWhen true
        } else {
            return@retryWhen false
        }
    }
}

fun <T> Flow<T>.retryOnNetworkConnection(
    connectivityMonitor: ConnectivityMonitor,
    retryHandler: (e: Throwable) -> Unit
): Flow<T> = flow {
    val exception = catchError(this)
    if (exception != null && exception is IOException) {
        retryHandler(exception)
        connectivityMonitor
            .isOnline
            .distinctUntilChanged()
            .collectLatest { isOnline ->
                if (isOnline) {
                    collect()
                }
            }
    }
}

sealed class RetryPolicy(
    val numRetries: Long,
    val delayMillis: Long,
    val delayFactor: Long
) {
    object DefaultRetryPolicy : RetryPolicy(numRetries = 6, delayMillis = 1000, delayFactor = 1)
}

@Suppress("NAME_SHADOWING")
internal suspend fun <T> Flow<T>.catchError(
    collector: FlowCollector<T>
): Throwable? {
    var fromDownstream: Throwable? = null
    try {
        collect {
            try {
                collector.emit(it)
            } catch (e: Throwable) {
                fromDownstream = e
                throw e
            }
        }
    } catch (e: Throwable) {
        val fromDownstream = fromDownstream
        if (e.isSameExceptionAs(fromDownstream) || e.isCancellationCause(coroutineContext)) {
            throw e
        } else {
            if (fromDownstream == null) {
                return e
            }
            if (e is CancellationException) {
                fromDownstream.addSuppressed(e)
                throw fromDownstream
            } else {
                e.addSuppressed(fromDownstream)
                throw e
            }
        }
    }
    return null
}

@OptIn(InternalCoroutinesApi::class)
private fun Throwable.isCancellationCause(coroutineContext: CoroutineContext): Boolean {
    val job = coroutineContext[Job]
    if (job == null || !job.isCancelled) return false
    return isSameExceptionAs(job.getCancellationException())
}

private fun Throwable.isSameExceptionAs(other: Throwable?): Boolean =
    other != null && other == this