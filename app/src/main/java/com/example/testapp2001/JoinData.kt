package com.example.testapp2001

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


data class JoinData(
    @field:SerializedName("userName") private val userName: String,
    @field:SerializedName("userEmail") private val userEmail: String,
    @field:SerializedName("userPwd") private val userPwd: String
)
class JoinResponse {
    @SerializedName("code")
    val code = 0

    @SerializedName("message")
    val message: String? = null

}

interface ServiceApi {
    @POST("/user/login")
    fun userLogin(@Body data: LoginData?): Call<LoginResponse?>?

    @POST("/user/join")
    fun userJoin(@Body data: JoinData?): Call<JoinResponse?>?

    @POST("/user/friends")
    fun findFriend(@Body data: UserData): Call<FriendlistResponse?>?



}