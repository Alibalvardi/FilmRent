package com.ali.filmrent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ali.filmrent.dataClass.Actor
import com.ali.filmrent.dataClass.Category
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.dataClass.Language
import com.ali.filmrent.databinding.ActivityMainBinding
import com.ali.filmrent.roomDatabase.ActorDao
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CategoryDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.LanguageDao

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var filmDao: FilmDao
    private lateinit var actorDao: ActorDao
    private lateinit var languageDao: LanguageDao
    private lateinit var categoryDao: CategoryDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //database
        filmDao = AppDatabase.getDatabase(this).filmDao
        actorDao = AppDatabase.getDatabase(this).actorDao
        languageDao = AppDatabase.getDatabase(this).languageDao
        categoryDao = AppDatabase.getDatabase(this).categoryDao


        //for insert all film to database in first run
//        val sharedPreferences = getSharedPreferences("firstRun", Context.MODE_PRIVATE)
//        if (sharedPreferences.getBoolean("first_run", true)) {
        firstRun()
//            sharedPreferences.edit().putBoolean("first_run", false).apply()
//        }


        binding.btnGoToManager.setOnClickListener {
            val intent = Intent(this, ManagerLoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoToCustomer.setOnClickListener {
            val intent = Intent(this, CustomerLoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun firstRun() {
        val filmList = listOf(
            Film(
                title = "Avatar: The Way of Water",
                yearOfRelease = 2022,
                language = Language(name = "English"),
                category = Category(name = "Action"),
                actor = Actor(name = "Sam Worthington"),
                rating = 3.5f,
                length = 192,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://m.media-amazon.com/images/M/MV5BYjhiNjBlODctY2ZiOC00YjVlLWFlNzAtNTVhNzM1YjI1NzMxXkEyXkFqcGdeQXVyMjQxNTE1MDA@._V1_FMjpg_UX1000_.jpg"
            ), Film(
                title = "Oppenheimer",
                yearOfRelease = 2023,
                language = Language(name = "English"),
                category = Category(name = "Thriller"),
                actor = Actor(name = "Cillian Murphy"),
                rating = 4.0f,
                length = 181,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://movies.universalpictures.com/media/opr-tsr1sheet3-look2-rgb-3-1-1-64545c0d15f1e-1.jpg"
            ), Film(
                title = "Deportees 2",
                yearOfRelease = 2009,
                language = Language(name = "persian"),
                category = Category(name = "Comedy"),
                actor = Actor(name = "Amin Hayai"),
                rating = 1.5f,
                length = 116,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://www.uptvs.com/wp-contents/uploads/2020/04/59580.jpg"
            ), Film(
                title = "Avengers: Endgame",
                yearOfRelease = 2019,
                language = Language(name = "English"),
                category = Category(name = "Sci-fi"),
                actor = Actor(name = "Robert Downey Jr."),
                rating = 4.5f,
                length = 182,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://m.media-amazon.com/images/I/512BgEoycmL._SX300_SY300_QL70_FMwebp_.jpg"
            ), Film(
                title = "La Haine",
                yearOfRelease = 1995,
                language = Language(name = "French"),
                category = Category(name = "Crime"),
                actor = Actor(name = "Vincent Cassel"),
                rating = 4.0f,
                length = 98,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://upload.wikimedia.org/wikipedia/en/3/30/Haine.jpg"
            ), Film(
                title = "Dune",
                yearOfRelease = 2021,
                language = Language(name = "English"),
                category = Category(name = "Sci-fi"),
                actor = Actor(name = "Timothée Chalamet"),
                rating = 4.0f,
                length = 145,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://upload.wikimedia.org/wikipedia/en/8/8e/Dune_%282021_film%29.jpg"
            ), Film(
                title = "Fast X",
                yearOfRelease = 2023,
                language = Language(name = "English"),
                category = Category(name = "Action"),
                actor = Actor(name = "Vin Diesel"),
                rating = 3.0f,
                length = 141,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://m.media-amazon.com/images/I/815AmXUcB5L._AC_UF1000,1000_QL80_.jpg"
            )
        )
        filmDao.insertListOfFilm(filmList)
        for (item in filmList) {
            actorDao.insertActor(item.actor)
            languageDao.insertLanguage(item.language)
            categoryDao.insertCategory(item.category)
        }

    }
}