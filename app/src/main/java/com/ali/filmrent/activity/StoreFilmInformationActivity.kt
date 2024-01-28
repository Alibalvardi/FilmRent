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
import com.ali.filmrent.roomDatabase.PaymentDao
import com.ali.filmrent.roomDatabase.RentalDao
import com.bumptech.glide.Glide

class StoreFilmInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreFilmInformationBinding
    private lateinit var film: Film
    private lateinit var filmDao: FilmDao
    private lateinit var rentalDao: RentalDao
    private lateinit var paymentDao: PaymentDao
    private lateinit var boughtInventoryDao: BoughtInventoryDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreFilmInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //first information
        filmDao = AppDatabase.getDatabase(this).filmDao
        paymentDao = AppDatabase.getDatabase(this).paymentDao
        rentalDao = AppDatabase.getDatabase(this).rentalDao
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
        binding.txtFilmCategory.text = film.category
        binding.txtFilmActor.text = film.actor
        binding.txtFilmLanguage.text = "Language : " + film.language
        binding.txtFilmDescription.text = film.description
        binding.itemRatingbarFilm.rating = film.rating

        Glide.with(this).load(film.urlImg).into(binding.imgFilm)

        binding.collapsingFilm.title = film.title

        binding.txtNumberOfFilm.text =
            "Number of movie copies purchased : " + boughtInventoryDao.countOfFilm(
                store_id,
                film.film_id!!
            ).toString()

        binding.txtNumberOfAvailableCope.text =
            "The number of available film copies : " + (boughtInventoryDao.getStoreFilmCopies(
                store_id,
                film.film_id!!
            ).size - rentalDao.countOfActiveRentsOfFilmInStore(store_id, film.film_id!!)).toString()
        binding.txtNumberOfRented.text =
            "The number of rented this film : " + rentalDao.countOfRentedOfStoreOnFilm(
                store_id,
                film.film_id!!
            )

        binding.txtNumberOfActiveRent.text =
            "The number of active rents : " + rentalDao.countOfActiveRentsOfFilmInStore(
                store_id,
                film.film_id!!
            )

        val rentedListOfStore = rentalDao.listRentedFilmOfStore(store_id)
        var countOfLate = 0
        for (rent in rentedListOfStore) {
            val duration = ((rent.returnDate - rent.rentalDate) / 86400000).toInt() + 1
            if (duration > rent.rentDuration && rent.film_id == film.film_id!!)
                countOfLate += 1
        }

        binding.txtNumberOfLate.text =
            "The number of late : $countOfLate"

        var amountPayment = 0
        val storePayments = paymentDao.listPaymentsOfStore(store_id)
        for (payment in storePayments) {
            if (film.film_id == rentalDao.getRentById(payment.rental_id).film_id && payment.customer_id != 0)
                amountPayment += payment.amount
        }

        binding.txtPayment.text =
            "Total amount received : $$amountPayment"


    }

}