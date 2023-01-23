package com.example.search

import com.example.data.common.database.bookmark.PhotoEntity
import com.example.data.common.database.history.SearchHistoryEntity
import com.example.data.common.ext.RandomString
import com.example.data.common.model.Photo
import com.example.ui.common.ext.create
import kotlin.random.Random

fun searchHistoriesEntityStub() = create(count = 10) { searchHistoryEntityStub() }
fun searchHistoryEntityStub() = SearchHistoryEntity(
    text = RandomString.next(),
    timestamp = RandomString.next()
)

fun photoEntitiesStub() = create(count = 10) { photoEntityStub() }

fun photoEntityStub() = PhotoEntity(
    id = RandomString.next(),
    title = RandomString.next(),
    imageUrl = RandomString.next(),
    localAddress = RandomString.next(),
    timestamp = RandomString.next()
)

fun photosStub() = create(count = 10) { photoStub() }

fun photoStub() = Photo(
    id = Random.nextInt().toString(),
    owner = RandomString.next(),
    secret = RandomString.next(),
    server = RandomString.next(),
    farm = RandomString.next(),
    title = RandomString.next(),
    isPublic = RandomString.next(),
    isFamily = RandomString.next(),
    isFriend = RandomString.next(),
)