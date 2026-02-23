package com.example.mobilegame

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    // Tells Retrofit to make a POST request to your-server.com/register
    @POST("/register")
    suspend fun register(@Body user: LogIn): Response<Void>

    // Tells Retrofit to make a POST request to your-server.com/login
    @POST("/login")
    suspend fun login(@Body user: LogIn): Response<Void>
}