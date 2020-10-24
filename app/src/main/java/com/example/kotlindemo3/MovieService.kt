package com.example.kotlindemo3

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movieServlet")
    suspend fun getMovieByTitle(@Query("title") title: String): Response<MoviesItem>

    @GET("movieServlet")
    suspend fun getMoviesPart(@Query("part") part: Int): Response<Movies>
}