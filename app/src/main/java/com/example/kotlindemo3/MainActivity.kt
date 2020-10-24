package com.example.kotlindemo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var retServiceMovies: MovieService
    private lateinit var retServiceVotes: VotesService

    lateinit var movies: Movies
    lateinit var votes: Votes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retServiceMovies = RetrofitInstance
            .getRetrofitInstance()
            .create(MovieService::class.java)

        retServiceVotes = RetrofitInstance
            .getRetrofitInstance()
            .create(VotesService::class.java)


        movies = getPartOfMovies()
        votes = getAllVotes()

        gridView.setOnItemClickListener { parent, view, possition, id ->
            val selectedItem = parent.getItemAtPosition(possition)
            val a: Model = selectedItem as Model
            val id = a.movieId.toString()
            val intent = Intent(this, SecondActivity::class.java)

            for (item in votes) {
                if (item.movie_id == id.toInt()) {
                    intent.putExtra("MyRating", item.score)
                    break;
                }
                intent.putExtra("MyRating", 0)
            }

            for (item in movies) {
                if (item.movie_id == id.toInt()) {
                    intent.putExtra("movie_id", item.movie_id)
                    intent.putExtra("overview", item.overview)
                    intent.putExtra("popularity", item.popularity)
                    intent.putExtra("poster", item.poster)
                    intent.putExtra("release_date", item.release_date)
                    intent.putExtra("runtime", item.runtime)
                    intent.putExtra("title", item.title)
                    intent.putExtra("tmdb_id", item.tmdb_id)
                    intent.putExtra("trailer", item.trailer)
                    intent.putExtra("vote_average", item.vote_average)
                    intent.putExtra("vote_count", item.vote_count)
                    startActivity(intent)
                    break
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorites -> {
                var db = DataBaseHandler(this)
                var data = db.readData()
                var list = mutableListOf<Model>()
                for (i in 0..data.size - 1) {
                    for (item in movies) {
                        if (item.movie_id == data[i]) {
                            list.add(
                                Model(
                                    "https://image.tmdb.org/t/p/w500" + item.poster,
                                    item.movie_id
                                )
                            )
                        }
                    }
                }
                gridView.adapter = MovieCustomTagAdapter(this@MainActivity, R.layout.row, list)
            }
            R.id.allMovies -> {
                var list = mutableListOf<Model>()
                for (item in movies) {
                    list.add(Model("https://image.tmdb.org/t/p/w500" + item.poster, item.movie_id))
                }
                gridView.adapter = MovieCustomTagAdapter(this@MainActivity, R.layout.row, list)
            }
            R.id.votes -> {
                for (item in votes) {
                    votes = getAllVotes()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getAllVotes(): Votes {
        var votes = Votes()
        var vote: VotesItem

        val responseLiveData: LiveData<Response<Votes>> = liveData {
            val response = retServiceVotes.getVotesByUserId(10)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val voteList = it.body()?.listIterator()
            if (voteList != null) {
                while (voteList.hasNext()) {
                    val movieItem = voteList.next()
                    vote = VotesItem(
                        movieItem.movie_id,
                        movieItem.score,
                        movieItem.user_id
                    )
                    votes.add(vote)
                }
            }
        })
        return votes
    }

    private fun getPartOfMovies(): Movies {
        var movies = Movies()
        var movie: MoviesItem
        val responseLiveData: LiveData<Response<Movies>> = liveData {
            val response = retServiceMovies.getMoviesPart(1)
            emit(response)

            if (response.isSuccessful) {
                var list = mutableListOf<Model>()
                for (item in movies) {
                    list.add(Model("https://image.tmdb.org/t/p/w500" + item.poster, item.movie_id))
                }
                gridView.adapter = MovieCustomTagAdapter(this@MainActivity, R.layout.row, list)
            }
        }

        responseLiveData.observe(this, Observer {
            val movieList = it.body()?.listIterator()
            if (movieList != null) {
                while (movieList.hasNext()) {
                    val movieItem = movieList.next()
                    movie = MoviesItem(
                        movieItem.movie_id,
                        movieItem.overview,
                        movieItem.popularity,
                        movieItem.poster,
                        movieItem.release_date,
                        movieItem.runtime,
                        movieItem.title,
                        movieItem.tmdb_id,
                        movieItem.trailer,
                        movieItem.vote_average,
                        movieItem.vote_count
                    )
                    movies.add(movie)
                }
            }
        })
        return movies
    }
}