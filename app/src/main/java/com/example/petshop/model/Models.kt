package com.example.petshop.model

import com.google.gson.annotations.SerializedName

// ─── Register ─────────────────────────────────────────────────────────────────

data class RegisterRequest(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class RegisterResponse(
    @SerializedName("message") val message: String,
    @SerializedName("user") val user: UserData?
)

// ─── Login ────────────────────────────────────────────────────────────────────

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: UserData?
)

// ─── User Data ────────────────────────────────────────────────────────────────

data class UserData(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String?
)

// ─── Profile ──────────────────────────────────────────────────────────────────

data class ProfileResponse(
    @SerializedName("message") val message: String,
    @SerializedName("profile") val profile: ProfileData?
)

data class ProfileData(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("created_at") val createdAt: String?
)

data class UpdateProfileRequest(
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String?
)

// ─── Change Password ──────────────────────────────────────────────────────────

data class ChangePasswordRequest(
    @SerializedName("current_password") val currentPassword: String,
    @SerializedName("new_password") val newPassword: String
)

// ─── Dashboard ────────────────────────────────────────────────────────────────

data class DashboardResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DashboardData?
)

data class DashboardData(
    @SerializedName("welcome_message") val welcomeMessage: String?,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("categories") val categories: List<CategoryItem>?
)

data class CategoryItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: String
)

// ─── Generic ──────────────────────────────────────────────────────────────────

data class MessageResponse(
    @SerializedName("message") val message: String
)
