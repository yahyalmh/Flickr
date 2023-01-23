package com.example.home

import com.example.data.common.database.bookmark.PhotoEntity
import com.example.ui.common.ext.RandomString
import com.example.ui.common.ext.create

fun photoEntitiesStub() = create(count = 10) { photoEntityStub() }

fun photoEntityStub() = PhotoEntity(
    id = RandomString.next(),
    title = RandomString.next(),
    imageUrl = RandomString.next(),
    localAddress = RandomString.next(),
    timestamp = RandomString.next()
)