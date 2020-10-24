package com.example.kotlindemo3

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity2.*
import retrofit2.Response

class SecondActivity : YouTubeBaseActivity() {

    var flag = 0
    lateinit var movie: MoviesItem
    var db = DataBaseHandler(this)
    var myRating = 0
    private lateinit var retServiceVotes: VotesService
    val YOUTUBE_API_KEY: String = "AIzaSyAGYLzqvpEt3RECmwamycY217516f6frug"


    lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity2)

        retServiceVotes = RetrofitInstance
            .getRetrofitInstance()
            .create(VotesService::class.java)


        val extras = intent.extras
        movie = MoviesItem(
            extras?.get("movie_id") as Int,
            extras?.get("overview") as String,
            extras?.get("popularity") as Double,
            extras?.get("poster") as String,
            extras?.get("release_date") as String,
            extras?.get("runtime") as Int,
            extras?.get("title") as String,
            extras?.get("tmdb_id") as Int,
            extras?.get("trailer") as String,
            extras?.get("vote_average") as Double,
            extras?.get("vote_count") as Int
        )
        val posterUrl = "https://image.tmdb.org/t/p/w500"

        Picasso.with(this).load(posterUrl + movie.poster).into(image)
        title1.text = movie.title
        overview.text = movie.overview
        textViewTime.text = movie.runtime.toString() + "'"
        textViewVoteAverage.text = movie.vote_average.toString()
        textViewReleaseDate.text = movie.release_date
        initUI(movie.trailer)
        var data = db.readData()
        for (i in 0..data.size - 1) {
            if (data[i] == movie.movie_id) {
                addToFavorites.setBackgroundResource(R.drawable.favorite_filled)
                flag = 1
            }
        }

        myRating = extras?.get("MyRating") as Int
        ratingBar.rating = myRating.toFloat() / 2
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (ratingBar.rating.toInt() * 2 != myRating) {
            //uploadVote()
        }
    }

    private fun initUI(videoId: String) {
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1?.loadVideo(videoId)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
            }
        }

        btnPlay.setOnClickListener(View.OnClickListener {
            youtubePlayer.initialize(YOUTUBE_API_KEY, youtubePlayerInit)
            btnPlay.visibility = View.INVISIBLE
        })

        addToFavorites.setOnClickListener {
            if (flag == 0) {
                addToFavorites.setBackgroundResource(R.drawable.favorite_filled)
                flag = 1
                db.insertData(movie.movie_id)
            } else {
                addToFavorites.setBackgroundResource(R.drawable.favorite)
                flag = 0
                db.deleteData(movie.movie_id)
            }
        }
    }

    private fun uploadVote() {
        val postResponse: LiveData<Response<VotesItem>> = liveData {
            val response = retServiceVotes.uploadVote(12)
            emit(response)
        }
    }
}



