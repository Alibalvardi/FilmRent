package com.ali.filmrent.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ali.filmrent.adapter.ActiveRentAdapter
import com.ali.filmrent.adapter.RentEvents
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.dataClass.Manager
import com.ali.filmrent.dataClass.Payment
import com.ali.filmrent.dataClass.Rating
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityCustomerActiveRentBinding
import com.ali.filmrent.databinding.RatingToStoreDialogBinding
import com.ali.filmrent.fragment.KEY_FILM_ID
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.ManagerDao
import com.ali.filmrent.roomDatabase.PaymentDao
import com.ali.filmrent.roomDatabase.RatingDao
import com.ali.filmrent.roomDatabase.RentalDao
import com.ali.filmrent.roomDatabase.StoreDao
import com.bumptech.glide.Glide
import java.util.Calendar

class CustomerActiveRentActivity : AppCompatActivity(), RentEvents {
    private lateinit var binding: ActivityCustomerActiveRentBinding
    private lateinit var filmDao: FilmDao
    private lateinit var film: Film
    private lateinit var manager: Manager
    private lateinit var customer: Customer
    private lateinit var rentalDao: RentalDao
    private lateinit var customerDao: CustomerDao
    private lateinit var storeDao: StoreDao
    private lateinit var managerDao: ManagerDao
    private lateinit var paymentDao: PaymentDao
    private lateinit var ratingDao: RatingDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerActiveRentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get start Information
        filmDao = AppDatabase.getDatabase(this).filmDao
        ratingDao = AppDatabase.getDatabase(this).ratingDao
        rentalDao = AppDatabase.getDatabase(this).rentalDao
        customerDao = AppDatabase.getDatabase(this).customerDao
        storeDao = AppDatabase.getDatabase(this).storeDao
        managerDao = AppDatabase.getDatabase(this).managerDao
        paymentDao = AppDatabase.getDatabase(this).paymentDao
        customer = customerDao.getCustomerById(intent.getIntExtra(KEY_CUSTOMER_ID, 0))
        film = filmDao.getFilmById(intent.getIntExtra(KEY_FILM_ID, 0))


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
    }

    private fun returnFilm(rent: Rental) {
        val startDate = Calendar.getInstance()
        startDate.timeInMillis = rent.rentalDate
        val nowDate = Calendar.getInstance()
        nowDate.timeInMillis = AppDatabase.getDatabase(this).calendarDao.getCalendar(1)
        val paymentAmount: Int
        val nowDuration: Int =
            ((nowDate.timeInMillis - startDate.timeInMillis) / 86400000).toInt() + 1
        if (nowDuration > rent.rentDuration) {
            val late: Int = (nowDuration / rent.rentDuration) * (nowDuration % rent.rentDuration)
            paymentAmount = (rent.rentDuration * 2) + (late * 3)
        } else {
            paymentAmount = (nowDuration * 2)
        }

        manager = managerDao.getManagerById(storeDao.getStoreManagerId(rent.store_id))

        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = "Return"
        dialog.confirmText = "Return"
        dialog.cancelText = "cancel"
        dialog.contentText = "You have to pay $$paymentAmount"
        dialog.setOnCancelListener {
            dialog.dismiss()
        }
        dialog.setConfirmClickListener {

            if (customer.wallet > paymentAmount) {
                rentalDao.insertReturnDate(rent.rental_id!!, nowDate.timeInMillis)
                customerDao.updateWallet(customer.customer_id!!, customer.wallet - paymentAmount)
                managerDao.updateWallet(manager.manager_id!!, manager.wallet + paymentAmount)
                paymentDao.insertPayment(
                    Payment(
                        customer_id = customer.customer_id!!,
                        store_id = rent.store_id,
                        rental_id = rent.rental_id,
                        amount = paymentAmount,
                        settlementDate = nowDate.timeInMillis
                    )
                )
                Toast.makeText(
                    this,
                    "The return of the film was done successfully",
                    Toast.LENGTH_LONG
                ).show()
                dialog.dismiss()
                rateToStore(storeDao.getStoreById(rent.store_id))
                showData()
            } else {
                Toast.makeText(this, "You need to increase your balance", Toast.LENGTH_LONG).show()
            }

        }
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun rateToStore(store:Store) {

        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = RatingToStoreDialogBinding.inflate(layoutInflater)
        dialogBinding.textView4.text = store.name + "  Store"
        dialogBinding.ratingBar.rating = 0f
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        if (ratingDao.ifCustomerRatedStore(store.store_id!!, customer.customer_id!!) == 0) {
            dialogBinding.ratingBar.rating = 0f
        } else {
            dialogBinding.ratingBar.rating =
                ratingDao.getRateIfRated(store.store_id, customer.customer_id!!)
        }

        dialogBinding.btnDone.setOnClickListener {


            if (ratingDao.ifCustomerRatedStore(store.store_id, customer.customer_id!!) == 0) {
                ratingDao.insertRating(
                    Rating(
                        rating = dialogBinding.ratingBar.rating,
                        store_id = store.store_id,
                        customer_id = customer.customer_id!!
                    )
                )
                Toast.makeText(this, "your rate was submit", Toast.LENGTH_SHORT).show()
            } else {
                ratingDao.updateRating(
                    dialogBinding.ratingBar.rating,
                    store_id = store.store_id,
                    customer_id = customer.customer_id!!
                )
                Toast.makeText(this, "your rate was updated", Toast.LENGTH_SHORT).show()
            }

            val storeRate: Float = ratingDao.avgStoreRating(store.store_id)
            storeDao.updateStoreRate(store.store_id, storeRate)
            dialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
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

        val listActiveRentsOfCustomer: List<Rental> =
            rentalDao.listActiveRentsOfCustomer(customer.customer_id!!, film.film_id!!)

        binding.txtNumberActiveRents.text =
            "The number of Active rents : " + listActiveRentsOfCustomer.size

        val adapter = ActiveRentAdapter(
            rents = ArrayList(listActiveRentsOfCustomer),

            this,
            true,
            AppDatabase.getDatabase(this)
        )
        binding.recycleActiveFilmCustomer.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycleActiveFilmCustomer.adapter = adapter


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    override fun onClickedItem(rental: Rental) {
        returnFilm(rental)
    }
}