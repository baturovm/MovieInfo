package dev.movie.info.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform
import dev.movie.info.R
import dev.movie.info.activities.MainActivity
import dev.movie.info.items.Movie
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {

    private lateinit var movie: Movie
    private lateinit var ctx: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
        movie = arguments?.getSerializable("movie") as Movie
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ctx = activity as MainActivity
        initView()
        toolbar_movie.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initView() {
        ViewCompat.setTransitionName(poster_movie, "end_container")
        Glide.with(requireContext())
            .load(movie.imageURL)
            .centerCrop()
            .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_broken_image_24))
            .into(poster_movie)
        toolbar_movie.title = movie.titleRU
        title_movie.text = movie.titleEN
        rating_movie.text = movie.rating.toString()
        year_movie.text = movie.year.toString()
        description_movie.text = if(movie.description.isNullOrEmpty()) "Нет описания" else movie.description
    }
}