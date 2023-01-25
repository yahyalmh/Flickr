package com.example.ui.common.utility

import android.content.Context
import android.graphics.Bitmap.CompressFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ImageDownloader @Inject constructor(
    private val imageLoader: ImageLoader,
    @ApplicationContext private val context: Context,
) {
    private lateinit var downloadJob: Deferred<ImageResult>

    suspend fun downloadToFiles(imageUrl: String, fileName: String): String? =
        withContext(Dispatchers.IO) {
            cancelCurrentJobIfExist()
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .build()
            downloadJob = imageLoader.enqueue(request).job

            return@withContext when (val result = downloadJob.await()) {
                is SuccessResult -> saveToFiles(result.drawable, fileName)
                is ErrorResult -> null
            }
        }

    private suspend fun cancelCurrentJobIfExist() {
        if (::downloadJob.isInitialized && downloadJob.isActive) {
            downloadJob.cancelAndJoin()
        }
    }

    private fun saveToFiles(drawable: Drawable, fileName: String): String? {
        return try {
            val bitmap = (drawable as BitmapDrawable).bitmap
            File(context.filesDir, "$fileName.jpeg").run {
                FileOutputStream(this).use {
                    bitmap.compress(CompressFormat.JPEG, 100, it)
                }
                absolutePath
            }
        } catch (e: Exception) {
            null
        }
    }
}