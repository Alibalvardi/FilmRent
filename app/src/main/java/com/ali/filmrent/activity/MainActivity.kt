package com.ali.filmrent.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ali.filmrent.dataClass.Actor
import com.ali.filmrent.dataClass.Category
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.dataClass.ImgStore
import com.ali.filmrent.dataClass.Language
import com.ali.filmrent.databinding.ActivityMainBinding
import com.ali.filmrent.roomDatabase.ActorDao
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CategoryDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.ImgStoreDao
import com.ali.filmrent.roomDatabase.LanguageDao

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var filmDao: FilmDao
    private lateinit var actorDao: ActorDao
    private lateinit var languageDao: LanguageDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var imgStoreDao: ImgStoreDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        database
        filmDao = AppDatabase.getDatabase(this).filmDao
        actorDao = AppDatabase.getDatabase(this).actorDao
        languageDao = AppDatabase.getDatabase(this).languageDao
        categoryDao = AppDatabase.getDatabase(this).categoryDao
        imgStoreDao = AppDatabase.getDatabase(this).imgStoreDao


        //for insert all film to database in first run
        val sharedPreferences = getSharedPreferences("firstRun", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_run", true)) {
            firstRun()
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }


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
                urlImg = "https://m.media-amazon.com/images/M/MV5BYjhiNjBlODctY2ZiOC00YjVlLWFlNzAtNTVhNzM1YjI1NzMxXkEyXkFqcGdeQXVyMjQxNTE1MDA@._V1_FMjpg_UX1000_.jpg",
                description = "Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a familiar threat returns to finish what was previously started, Jake must work with Neytiri and the army of the Na'vi race to protect their home."
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
                urlImg = "https://movies.universalpictures.com/media/opr-tsr1sheet3-look2-rgb-3-1-1-64545c0d15f1e-1.jpg",
                description = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb."
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
                urlImg = "https://www.uptvs.com/wp-contents/uploads/2020/04/59580.jpg",
                description = "In this sequel the deportees are now captured by the Iraqis and are living in a camp. On the other hand their families who are going to Mashad are imprisoned when their airplane is hijacked by some terrorists who want to take it to Iraq. In the camp also they have to deal with the Iraqis who are imprisoned them."
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
                urlImg = "https://m.media-amazon.com/images/I/512BgEoycmL._SX300_SY300_QL70_FMwebp_.jpg",
                description = "After the devastating events of Avengers: Infinity War (2018), the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe."
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
                urlImg = "https://upload.wikimedia.org/wikipedia/en/3/30/Haine.jpg",
                description = "24 hours in the lives of three young men in the French suburbs the day after a violent riot."
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
                urlImg = "https://upload.wikimedia.org/wikipedia/en/8/8e/Dune_%282021_film%29.jpg",
                description = "A noble family becomes embroiled in a war for control over the galaxy's most valuable asset while its heir becomes troubled by visions of a dark future."
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
                urlImg = "https://m.media-amazon.com/images/I/815AmXUcB5L._AC_UF1000,1000_QL80_.jpg",
                description = "Dom Toretto and his family are targeted by the vengeful son of drug kingpin Hernan Reyes."
            ), Film(
                title = " The Shawshank Redemption",
                yearOfRelease = 1994,
                language = Language(name = "English"),
                category = Category(name = "Drama"),
                actor = Actor(name = "Tim Robbins"),
                rating = 4.5f,
                length = 144,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://upload.wikimedia.org/wikipedia/en/8/81/ShawshankRedemptionMoviePoster.jpg",
                description = "Over the course of several years, two convicts form a friendship, seeking consolation and, eventually, redemption through basic compassion."
            ), Film(
                title = "Sausage Party",
                yearOfRelease = 2016,
                language = Language(name = "English"),
                category = Category(name = "Animation"),
                actor = Actor(name = "Seth Rogen"),
                rating = 3.0f,
                length = 89,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://upload.wikimedia.org/wikipedia/en/thumb/f/f7/Sausage_Party_logo.png/220px-Sausage_Party_logo.png",
                description = "A sausage strives to discover the truth about his existence."
            ), Film(
                title = "The Holdovers",
                yearOfRelease = 2023,
                language = Language(name = "English"),
                category = Category(name = "Drama"),
                actor = Actor(name = "Paul Giamatti"),
                rating = 4.0f,
                length = 133,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://digimovie7.top/wp-content/uploads/2023/11/ylXrhRxZgs80gIBlfS97Xu73uCa.jpg",
                description = "A cranky history teacher at a remote prep school is forced to remain on campus over the holidays with a troubled student who has no place to go."
            ), Film(
                title = "The Godfather",
                yearOfRelease = 1972,
                language = Language(name = "English"),
                category = Category(name = "Crime"),
                actor = Actor(name = "AL Pacino"),
                rating = 4.6f,
                length = 175,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://digimovie7.top/wp-content/uploads/2020/05/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._UX500.jpg",
                description = "Don Vito Corleone, head of a mafia family, decides to hand over his empire to his youngest son Michael. However, his decision unintentionally puts the lives of his loved ones in grave danger."
            ), Film(
                title = "12 Angry Men",
                yearOfRelease = 1957,
                language = Language(name = "English"),
                category = Category(name = "Drama"),
                actor = Actor(name = "Henry FondaLee"),
                rating = 4.5f,
                length = 96,
                rentDuration = 14,
                rentPerDay = 2,
                amount = 10,
                urlImg = "https://digimovie7.top/wp-content/uploads/2020/05/572.jpg",
                description = "The jury in a New York City murder trial is frustrated by a single member whose skeptical caution forces them to more carefully consider the evidence before jumping to a hasty verdict."
            )
        )
        val urlStoreList = listOf(
            ImgStore("https://www.uplooder.net/img/image/88/8c7233914bcfb1d2f7f1b5e233d5231e/1-(1).jpg"),
            ImgStore("https://www.uplooder.net/img/image/59/efa1789b492c483aac3f17d03cc2fa6f/20190218-122101-777x437.jpg"),
            ImgStore("https://www.uplooder.net/img/image/62/cc3f40fd7d3b62e611252696721a7ced/yore-movie-w.jpg"),
            ImgStore("https://www.uplooder.net/img/image/22/267bca6d3a74f4aaf141f3f1d557bc37/download-(1).jpg"),
            ImgStore("https://www.uplooder.net/files/c468432b6ccdf56a19c7214c953a9603/1701209964336.webp.html"),
            ImgStore("https://www.uplooder.net/img/image/13/7a097eb4e7d19bd669086b605bb99625/image-a-s-set.jpeg"),
            ImgStore("https://www.uplooder.net/img/image/42/893a7acb724bdbfa0d0f7cc2d703f4ab/download.jpg"),
            ImgStore("https://www.uplooder.net/img/image/84/deaef6647ca8f1e1f47696d7ffc889ff/image.jpg"),
            ImgStore("https://www.uplooder.net/img/image/38/8fbeceac81bf7e34488efd5628127b59/v4blh3ysbyc71.jpg"),
            ImgStore("https://www.uplooder.net/img/image/81/b4a14c17f1943b026d7168dba636d4c1/1-(1).jpg")
        )
        filmDao.insertListOfFilm(filmList)
        imgStoreDao.insertListOfUrl(urlStoreList)
        for (item in filmList) {
            actorDao.insertActor(item.actor)
            languageDao.insertLanguage(item.language)
            categoryDao.insertCategory(item.category)
        }

    }
}
