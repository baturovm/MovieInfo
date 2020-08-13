package dev.movie.info.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dev.movie.info.R

class MainActivity : AppCompatActivity() {


    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "Загрузка...", Toast.LENGTH_LONG).show()
        navController = findNavController(R.id.nav_host_fragment)
    }
}