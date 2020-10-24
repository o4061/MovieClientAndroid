package com.example.kotlindemo3

data class MoviesItem(
    var movie_id: Int,
    var overview: String,
    var popularity: Double,
    var poster: String,
    var release_date: String,
    var runtime: Int,
    var title: String,
    var tmdb_id: Int,
    var trailer: String,
    var vote_average: Double,
    var vote_count: Int
)