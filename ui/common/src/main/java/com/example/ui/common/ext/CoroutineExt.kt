package com.example.ui.common.ext

import com.example.ui.common.utility.JobDisposable
import kotlinx.coroutines.Job


fun Job.addTo(jobDisposable: JobDisposable) = jobDisposable.add(this)