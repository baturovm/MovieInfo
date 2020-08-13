package dev.movie.info.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialSharedAxis
import dev.movie.info.R
import dev.movie.info.activities.MainActivity
import dev.movie.info.adapters.MovieListAdapter
import dev.movie.info.items.Movie
import dev.movie.info.items.MoviesObject
import dev.movie.info.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_movie_list.*

class MovieListFragment : Fragment() {

    private val model: MainViewModel by activityViewModels()
    private lateinit var adapter: MovieListAdapter
    private lateinit var ctx: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(
            MaterialSharedAxis.Z,true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        model.moviesObject.observe(viewLifecycleOwner, Observer {
            when {
                it == null -> showPlaceholder("Не удалось получить список!")
                it.movies.isEmpty() -> showPlaceholder("Пусто")
                else -> initView(it)
            }
        })
        model.filteredList.observe(viewLifecycleOwner, Observer {
            showList(true)
            updateList(it)
        })
    }

    private fun init() {
        ctx = activity as MainActivity
        adapter = MovieListAdapter(ctx)
        rv_movie_list.adapter = adapter
    }

    //Запоняем данные и ставим слушатель chips
    private fun initView(moviesObject: MoviesObject) {
        updateList(moviesObject.movies)
        createChips(moviesObject.genres)
        chip_group_filter.setOnCheckedChangeListener { _, checkedId ->
            val chip: Chip? = ctx.findViewById(checkedId)
            if(chip==null) {
                updateList(model.getMovies())
            } else {
                model.filterData(chip.text as String)
            }
        }
    }

    //Обновление списка и скрытие Placeholder
    private fun updateList(list: List<Movie>) {
        adapter.items = list
        adapter.notifyDataSetChanged()
    }

    //Создаем chips на основе списка жанров
    private fun createChips(genres: List<String>) {
        for(i in genres.indices) {
            val chip = layoutInflater.inflate(R.layout.chip_layout, chip_group_filter, false) as Chip
            chip.apply {
                text = genres[i]
                id = i
            }
            chip_group_filter.addView(chip)
        }
    }

    //Ошибка или список пуст, показываем Placeholder
    private fun showPlaceholder(error: String) {
        showList(false)
        error_text_movie_list.text = error
    }

    //Показываем список, скрывая Placeholder
    private fun showList(value: Boolean) {
        rv_movie_list.setVisibility(value)
        placeholder_movie_list.setVisibility(!value)
    }

    private fun View.setVisibility(value: Boolean) {
        if(value) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }
}