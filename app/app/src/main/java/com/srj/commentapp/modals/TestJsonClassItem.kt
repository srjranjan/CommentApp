package com.srj.commentapp.modals


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TestJsonClassItem(
    @Json(name = "comment")
    val comment: String, // All test cases passed.Status: 200 OK
    @Json(name = "email")
    val email: String, // srjranjan@gmail.com
    @Json(name = "_id")
    val id: String, // 620a15553bd4c78acac52c5d
    @Json(name = "__v")
    val v: Int // 0
)