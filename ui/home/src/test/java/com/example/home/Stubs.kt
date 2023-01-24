package com.example.home

import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.ext.RandomString
import com.example.ui.common.ext.create

fun photoEntitiesStub() = create(count = 10) { photoEntityStub() }

fun photoEntityStub() = PhotoEntity(
    id = RandomString(),
    title = RandomString(),
    imageUrl = RandomString(),
    localAddress = RandomString(),
    timestamp = RandomString()
)