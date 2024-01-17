package com.ali.filmrent.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.databinding.ActivityStoreFilmInformationBinding
import com.ali.filmrent.fragment.KEY_FILM_ID
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.bumptech.glide.Glide

class StoreFilmInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreFilmInformationBinding
    private lateinit var film: Film
    private lateinit var filmDao: FilmDao
    private lateinit var boughtInventoryDao: BoughtInventoryDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreFilmInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //first information
        filmDao = AppDatabase.getDatabase(this).filmDao
        boughtInventoryDao = AppDatabase.getDatabase(this).boughtInventoryDao
        val film_id: Int = intent.getIntExtra(KEY_FILM_ID, 0)
        val store_id = intent.getIntExtra(KEY_STORE_ID, 0)
        film = filmDao.getFilmById(film_id)

        setSupportActionBar(binding.toolbarFilm)
        binding.collapsingFilm.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        showData(store_id)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun showData(store_id: Int) {
        binding.txtFilmTitle.text = film.title
        binding.txtFilmYear.text = film.yearOfRelease.toString()
        binding.txtFilmLength.text = film.length.toString() + " min"
        binding.txtFilmCategory.text = film.category.name
        binding.txtFilmActor.text = film.actor.name
        binding.txtFilmLanguage.text = "Language : " + film.language.name
        binding.txtFilmDescription.text = film.description
        binding.itemRatingbarFilm.rating = film.rating

        Glide.with(this).load(film.urlImg).into(binding.imgFilm)

        binding.collapsingFilm.title = film.title

        binding.txtNumberOfFilm.text =
            "Number of movie copies purchased : " + boughtInventoryDao.countOfFilm(
                store_id,
                film.film_id!!
            ).toString()


    }

}