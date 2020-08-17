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
            initView(it)
        })
        model.filteredList.observe(viewLifecycleOwner, Observer {
            if (it==null) {
                updateList(model.getMovies())
            } else {
                updateList(it)
            }
        })
        chip_group_filter.setOnCheckedChangeListener { _, checkedId ->
            val chip: Chip? = ctx.findViewById(checkedId)
            if(chip==null) { //Ничего не выбрано
                model.filteredList.value = null
            } else { //Выбран жанр
                model.filterData(chip.text.toString())
            }
        }
    }

    private fun init() {
        ctx = activity as MainActivity
        adapter = MovieListAdapter(ctx)
        rv_movie_list.adapter = adapter
    }

    //Запоняем данные
    private fun initView(moviesObject: MoviesObject?) {
        when {
            moviesObject == null -> showPlaceholder("Не удалось получить список!")
            moviesObject.movies.isEmpty() -> showPlaceholder("Пусто")
            else -> {
                updateList(moviesObject.movies)
                createChips(moviesObject.genres)
            }
        }
    }

    //Обновление списка и скрытие Placeholder
    private fun updateList(list: List<Movie>) {
        showList(true)
        adapter.setData(list)
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

    //Показываем список, скрывая Placeholder
    private fun showList(value: Boolean) {
        rv_movie_list.setVisibility(value)
        placeholder_movie_list.setVisibility(!value)
    }

    //Ошибка или список пуст, показываем Placeholder
    private fun showPlaceholder(error: String) {
        showList(false)
        error_text_movie_list.text = error
    }

    private fun View.setVisibility(value: Boolean) {
        if(value) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }
}