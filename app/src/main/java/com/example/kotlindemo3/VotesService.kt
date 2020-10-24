package com.example.kotlindemo3

import retrofit2.Response
import retrofit2.http.*

interface VotesService {

    @GET("voteServlet")
    suspend fun getVotesByUserId(@Query("user_id") user: Int): Response<Votes>

    @POST("voteServlet")
    suspend fun uploadVote(@Body userId: Int): Response<VotesItem>
}