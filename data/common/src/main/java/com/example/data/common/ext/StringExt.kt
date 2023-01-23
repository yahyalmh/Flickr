package com.example.data.common.ext

object RandomString {
    fun next(length: Int = 10, withNumbers: Boolean = true): String {
        val charsSet = (('A'..'Z') + ('a'..'z')).toMutableList()
        if (withNumbers) {
            charsSet += ('0'..'9')
        }
        return (1..length)
            .map { charsSet.random() }
            .joinToString("")
    }
}
