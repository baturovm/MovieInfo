package dev.movie.info.api

import dev.movie.info.items.MoviesObject
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/sequeniatesttask/films.json")
    fun getMovies(): Call<MoviesObject>
}