package com.example.submission3.api


import com.example.submission3.response.SearchingResponse
import com.example.submission3.response.UserDetailResponse
import com.example.submission3.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {


    @GET("search/users")
    @Headers("Authorization: token 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", "UserResponse-Agent: request")
    fun getSearchUser(
        @Query("q") id: String
    ): Call<SearchingResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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", "UserResponse-Agent: request")
    fun getFollowers(
        @Path("username") id: String
    ): Call<List<UserResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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", "UserResponse-Agent: request")
    fun getFollowing(
        @Path("username") id: String
    ): Call<List<UserResponse>>

    @GET("users/{username}")
    @Headers("Authorization: token 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", "UserResponse-Agent: request")
    fun getDetail(
        @Path("username") id: String
    ): Call<UserDetailResponse>
}