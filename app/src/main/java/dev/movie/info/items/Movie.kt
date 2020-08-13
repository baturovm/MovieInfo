package dev.movie.info.items

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie (
    @SerializedName("id")
    val id : Int,
    @SerializedName("localized_name")
    val titleRU : String,
    @SerializedName("name")
    val titleEN: String,
    @SerializedName("year")
    val year : Int,
    @SerializedName("rating")
    val rating : Double,
    @SerializedName("image_url")
    val imageURL : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("genres")
    val genres : List<String>
) : Serializable