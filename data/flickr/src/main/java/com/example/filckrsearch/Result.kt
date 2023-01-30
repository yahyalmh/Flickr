package com.example.filckrsearch

import com.example.filckrsearch.Result.*
import com.example.filckrsearch.model.FlickrResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Response

sealed interface Result<T> {
    class Success<T>(val data: T) : Result<T>
    class Error<T>(val code: Int? = null, val message: String? = null) : Result<T>
    class Exception<T>(val e: Throwable) : Result<T>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Success(it)
        }
        .catch { emit(Error(message = it.message)) }
}

fun <T : FlickrResponse?> Response<T>.asResult(): Result<T> = try {
    val responseBody = body()
    if (isSuccessful && responseBody != null) {
        val gson = Gson()
        val type = object : TypeToken<FlickrResponse.ErrorResponse>() {}.type
        var errorResponse: FlickrResponse.ErrorResponse? = gson.fromJson(errorBody()!!.charStream(), type)

        when (ResponseType.fromString(responseBody.status)) {
            ResponseType.OK -> Success(responseBody)
            ResponseType.FAIL -> Error()
        }
    } else {
        Error(code = code(), message = message())
    }
} catch (e: HttpException) {
    Error(code = e.code(), message = e.message())
} catch (e: Throwable) {
    Exception(e)
}

enum class ResponseType(val value: String) {
    FAIL("fail"),
    OK("ok");

    companion object {
        fun fromString(value: String) =
            ResponseType.values().firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("$value is not a valid response type")
    }
}
