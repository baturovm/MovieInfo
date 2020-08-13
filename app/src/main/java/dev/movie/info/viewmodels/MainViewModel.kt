package dev.movie.info.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.movie.info.api.ApiService
import dev.movie.info.items.Movie
import dev.movie.info.items.MoviesObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    val moviesObject = MutableLiveData<MoviesObject>()
    val filteredList = MutableLiveData<List<Movie>>()

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://s3-eu-west-1.amazonaws.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var apiService: ApiService


    init {
        apiService = retrofit.create(ApiService::class.java)
        fetchMovies()
    }


    //Загружаем список фильмов
    private fun fetchMovies() {
        apiService.getMovies().enqueue(object : Callback<MoviesObject> {
            override fun onFailure(call: Call<MoviesObject>, t: Throwable) {
                moviesObject.value = null
            }

            override fun onResponse(call: Call<MoviesObject>, response: Response<MoviesObject>) {
                mapData(response.body())
            }
        })
    }


    //Подготовка данных для MovieListFragment
    fun mapData(data: MoviesObject?) {
        viewModelScope.launch(Dispatchers.IO) {
            if(data!=null) {
                data.movies = data.movies.sortedBy { it.titleRU }
                data.genres = getGenres(data.movies)
                moviesObject.postValue(data)
            } else {
                moviesObject.postValue(null)
            }
        }
    }


    //Достаем список жанров
    private fun getGenres(list: List<Movie>): List<String> {
        val genres = mutableListOf<String>()
        for(item in list) {
           genres.addAll(item.genres)
        }
        return genres.distinct()
    }


    //Фильтр данных по жанру
    fun filterData(genre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mutableListOf<Movie>()
            for(item in getMovies()) {
                if(item.genres.contains(genre)) {
                    list.add(item)
                }
            }
            filteredList.postValue(list)
        }
    }

    fun getMovies(): List<Movie> {
        return moviesObject.value?.movies ?: emptyList()
    }
}