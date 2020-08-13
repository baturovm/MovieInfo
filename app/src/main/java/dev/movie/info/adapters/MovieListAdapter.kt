package dev.movie.info.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.movie.info.R
import dev.movie.info.activities.MainActivity
import dev.movie.info.items.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieListAdapter(val ctx: MainActivity) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {

    var items: List<Movie> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         //Заполнение списка фильмов
        fun bind(item: Movie) {
            itemView.title_movie_item.text = item.titleRU
             ViewCompat.setTransitionName(itemView.poster_movie_item, "trans$adapterPosition")
            Glide.with(itemView)
                .load(item.imageURL)
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(ctx, R.drawable.ic_baseline_broken_image_24))
                .into(itemView.poster_movie_item)
            itemView.setOnClickListener {
                val bundle = Bundle().apply {
                        putSerializable("movie", item)
                    }
                val extras = FragmentNavigatorExtras(itemView.poster_movie_item to "end_container")
                ctx.navController.navigate(R.id.movieFragment, bundle, null, extras)
            }
        }
    }

}
