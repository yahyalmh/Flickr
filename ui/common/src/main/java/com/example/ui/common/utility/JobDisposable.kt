package com.example.ui.common.utility

import kotlinx.coroutines.Job

class JobDisposable {
    private val disposables by lazy { mutableListOf<Job>() }

    fun add(job: Job) = job.also { disposables.add(it) }
    fun dispose() = disposables.forEach { it.cancel() }
}