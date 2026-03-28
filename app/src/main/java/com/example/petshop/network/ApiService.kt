package com.example.petshop.network

import com.example.petshop.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // POST /api/register
    @POST("register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    // POST /api/login
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // GET /api/dashboard
    @GET("dashboard")
    fun getDashboard(): Call<DashboardResponse>

    // GET /api/profile
    @GET("profile")
    fun getProfile(): Call<ProfileResponse>

    // PUT /api/profile
    @PUT("profile")
    fun updateProfile(@Body request: UpdateProfileRequest): Call<ProfileResponse>

    // POST /api/change-password
    @POST("change-password")
    fun changePassword(@Body request: ChangePasswordRequest): Call<MessageResponse>
}
