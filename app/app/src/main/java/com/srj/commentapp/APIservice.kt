package com.srj.commentapp

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIservice {

    @POST("/auth/signup")
    fun signup(@Body request: RequestBody): Response<ResponseBody>

    @POST("/auth/login")
    fun login(@Body request: RequestBody): Response<ResponseBody>

    @POST("/auth/logout")
    fun logout(@Body request: RequestBody): Response<ResponseBody>

    @POST("/auth/forgot-password")
    fun forgotPassword(@Body request: RequestBody): Response<ResponseBody>

    @POST("/post-comment")
    fun submitComment(@Body request: RequestBody): Response<ResponseBody>

    @GET("/view-comments")
    fun viewComment(): Response<List<CommentModal>>


}