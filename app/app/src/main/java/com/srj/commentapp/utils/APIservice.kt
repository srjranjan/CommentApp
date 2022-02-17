package com.srj.commentapp.utils

import com.srj.commentapp.PasswordModal
import com.srj.commentapp.modals.TestJsonClass
import com.srj.commentapp.postComment
import com.srj.commentapp.responseMessage
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIservice {

    @POST("/auth/signup")
    suspend fun signup(@Body request: RequestBody): Response<responseMessage>

    @POST("/auth/login")
    suspend fun login(@Body request: RequestBody): Response<responseMessage>

    @POST("/auth/logout")
    suspend fun logout(@Body request: RequestBody): Response<ResponseBody>

    @POST("/auth/forgot-password")
    suspend fun forgotPassword(@Body request: RequestBody): Response<PasswordModal>

    @POST("/post-comment")
    suspend fun submitComment(@Body request: RequestBody): Response<postComment>


    @Headers("Content-Type:application/json")
    @GET("/view-comments")
    suspend fun viewComment(): Response<TestJsonClass>


}