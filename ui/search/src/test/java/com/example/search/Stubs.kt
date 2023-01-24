package com.example.search

import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.database.history.SearchHistoryEntity
import com.example.data.common.ext.RandomString
import com.example.data.common.model.Photo
import com.example.ui.common.ext.create
import kotlin.random.Random

fun searchHistoriesEntityStub() = create(count = 10) { searchHistoryEntityStub() }
fun searchHistoryEntityStub() = SearchHistoryEntity(
    text = RandomString(),
    timestamp = RandomString()
)

fun photoEntitiesStub() = create(count = 10) { photoEntityStub() }

fun photoEntityStub() = PhotoEntity(
    id = RandomString(),
    title = RandomString(),
    imageUrl = RandomString(),
    localAddress = RandomString(),
    timestamp = RandomString()
)

fun photosStub() = create(count = 10) { photoStub() }

fun photoStub() = Photo(
    id = Random.nextInt().toString(),
    owner = RandomString(),
    secret = RandomString(),
    server = RandomString(),
    farm = RandomString(),
    title = RandomString(),
    isPublic = RandomString(),
    isFamily = RandomString(),
    isFriend = RandomString(),
)