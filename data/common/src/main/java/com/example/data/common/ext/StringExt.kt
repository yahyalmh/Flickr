package com.example.data.common.ext

object RandomString {
    private val charsSet = (('A'..'Z') + ('a'..'z')).toMutableList()

    operator fun invoke(length: Int = 10, withNumbers: Boolean = true): String {
        if (withNumbers) {
            charsSet += ('0'..'9')
        }
        return (1..length)
            .map { charsSet.random() }
            .joinToString("")
    }
}
