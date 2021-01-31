package com.example.testapp2001

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginData(
        @field:SerializedName("userEmail") private val userName: String,
        @field:SerializedName("userPwd") private val userPwd: String
)

class LoginResponse {
    @SerializedName("code")
    val code = 0

    @SerializedName("message")
    val message: String? = null

    @SerializedName("userId")
    val userId = 0

}

data class UserData(
        @field:SerializedName("useruid") private val useruid: Int
)
class  friendrelation(
        @field:SerializedName("User1Uid")  val user1uid: Int,
        @field:SerializedName("User1Name")  val user1name: String,
        @field:SerializedName("User2Uid")  val user2uid: Int,
        @field:SerializedName("User2Name")  val user2name: String
)
class FriendlistResponse {
    @SerializedName("code")
    val code = 0

    @SerializedName("message")
    val message: String? = null

    @SerializedName("userlist")
    val userlist : Array<friendrelation>? =null

}