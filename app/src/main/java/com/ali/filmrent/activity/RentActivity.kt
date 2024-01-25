package com.ali.filmrent.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Reserve
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityRentBinding
import com.ali.filmrent.fragment.KEY_FILM_ID
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.RentalDao
import com.ali.filmrent.roomDatabase.ReserveDao
import com.ali.filmrent.roomDatabase.StoreDao
import com.bumptech.glide.Glide
import java.util.Calendar

class RentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRentBinding
    private lateinit var rentalDao: RentalDao
    private lateinit var customerDao: CustomerDao
    private lateinit var storeDao: StoreDao
    private lateinit var filmDao: FilmDao
    private lateinit var reserveDao: ReserveDao
    private lateinit var boughtInventoryDao: BoughtInventoryDao
    private lateinit var customer: Customer
    private lateinit var store: Store
    private lateinit var film: Film

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //start information
        filmDao = AppDatabase.getDatabase(this).filmDao
        storeDao = AppDatabase.getDatabase(this).storeDao
        customerDao = AppDatabase.getDatabase(this).customerDao
        rentalDao = AppDatabase.getDatabase(this).rentalDao
        boughtInventoryDao = AppDatabase.getDatabase(this).boughtInventoryDao
        reserveDao = AppDatabase.getDatabase(this).reserveDao
        val filmId = intent.getIntExtra(KEY_FILM_ID, 0)
        val storeId = intent.getIntExtra(KEY_STORE_ID, 0)
        val customerId = intent.getIntExtra(KEY_CUSTOMER_ID, 0)
        store = storeDao.getStoreById(storeId)
        film = filmDao.getFilmById(filmId)
        customer = customerDao.getCustomerById(customerId)


        setSupportActionBar(binding.toolbarFilm)
        binding.collapsingFilm.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        showData()

        binding.btnRent.setOnClickListener {
            val cntAvlCopies: Int = boughtInventoryDao.countOfFilm(
                store.store_id!!,
                film.film_id!!
            ) - rentalDao.countOfActiveRentsOfFilm(store.store_id!!, film.film_id!!)
            if (cntAvlCopies > 0) {
                rentFilm()
            } else {
                Toast.makeText(
                    this,
                    "There is no copy available, reserve if need it",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.btnBook.setOnClickListener {
            val cntAvlCopies: Int = boughtInventoryDao.countOfFilm(
                store.store_id!!,
                film.film_id!!
            ) - rentalDao.countOfActiveRentsOfFilm(store.store_id!!, film.film_id!!)
            if (cntAvlCopies > 0) {
                Toast.makeText(
                    this,
                    "There is a available copy, rent if need it",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                bookFilm()
            }
        }

    }

    private fun bookFilm() {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = "Book"
        dialog.confirmText = "Reserve"
        dialog.cancelText = "cancel"
        dialog.contentText = "Book this film?"
        dialog.setOnCancelListener {
            dialog.dismiss()
        }
        dialog.setConfirmClickListener {
            if (reserveDao.countOfReserveOfCustomerFromStoreForFilm(
                    customer.customer_id!!,
                    film.film_id!!,
                    store.store_id!!,

                    ) == 0
            ) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis =
                    (AppDatabase.getDatabase(this).calendarDao).getCalendar(1)

                reserveDao.insertReserve(
                    Reserve(
                        customer_id = customer.customer_id!!,
                        store_id = store.store_id!!,
                        film_id = film.film_id!!,
                        reserveDate = calendar.timeInMillis
                    )
                )

                Toast.makeText(this, "Reserved successfully", Toast.LENGTH_LONG).show()
                showData()
                dialog.dismiss()
                onBackPressed()
            } else {
                dialog.dismiss()
                Toast.makeText(this, "You reserved this film from this store", Toast.LENGTH_LONG)
                    .show()
            }

        }
        dialog.show()
    }

    private fun rentFilm() {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = "Rent"
        dialog.confirmText = "Rent"
        dialog.cancelText = "cancel"
        dialog.contentText = "Rent this film?"
        dialog.setOnCancelListener {
            dialog.dismiss()
        }
        dialog.setConfirmClickListener {
            if (rentalDao.countOfActiveRentsOfCustomerFromStore(
                    store.store_id!!,
                    customer.customer_id!!
                ) < 3
            ) {
                val rentCalendar = Calendar.getInstance()
                rentCalendar.timeInMillis =
                    (AppDatabase.getDatabase(this).calendarDao).getCalendar(1)

                rentalDao.insertRental(
                    Rental(
                        store_id = store.store_id!!,
                        customer_id = customer.customer_id!!,
                        film_id = film.film_id!!,
                        rentalDate = rentCalendar.timeInMillis,
                        returnDate = 0,
                        rentDuration = 14
                    )
                )
                Toast.makeText(this, " Rented successfully", Toast.LENGTH_LONG).show()
                showData()
                dialog.dismiss()
                onBackPressed()
            } else {
                dialog.dismiss()
                Toast.makeText(this, "You have 3 active rents from this store", Toast.LENGTH_LONG)
                    .show()
            }

        }
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
        binding.btnBook.text = "Reserve"
        binding.txtFilmTitle.text = film.title
        binding.txtFilmYear.text = film.yearOfRelease.toString()
        binding.txtFilmLength.text = film.length.toString() + " min"
        binding.txtFilmCategory.text = film.category.name
        binding.txtFilmActor.text = film.actor.name
        binding.txtFilmLanguage.text = "Language : " + film.language.name
        binding.txtFilmDescription.text = film.description
        binding.itemRatingbarFilm.rating = film.rating

        val manager =
            (AppDatabase.getDatabase(this).managerDao).getManagerById(store.manager_id)
        binding.txtStoreName.text = "Store name : " + store.name
        binding.txtStorePhone.text = "Phone : " + store.phoneNumber
        binding.txtStoreManagerName.text =
            "Manager : " + manager.firstname + " " + manager.lastname
        binding.txtManagerPhone.text = "Manager Phone : " + manager.phoneNumber
        binding.itemRatingBarStore.rating = store.rating

        Glide.with(this).load(film.urlImg).into(binding.imgFilm)

        binding.collapsingFilm.title = film.title

        binding.txtNumberAvailableCopeis.text =
            "The number of available film copies : " + (boughtInventoryDao.countOfFilm(
                store.store_id!!,
                film.film_id!!
            ) - rentalDao.countOfActiveRentsOfFilm(store.store_id!!, film.film_id!!)).toString()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

}