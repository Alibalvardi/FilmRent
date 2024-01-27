package com.ali.filmrent.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.R
import com.ali.filmrent.adapter.ActiveRentAdapter
import com.ali.filmrent.adapter.RentEvents
import com.ali.filmrent.adapter.RentedFilmAdapter
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityActiveRentsStoreBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.RentalDao
import com.ali.filmrent.roomDatabase.StoreDao

class CustomerRentedFilmActivity : AppCompatActivity(), RentEvents {
    private lateinit var binding: ActivityActiveRentsStoreBinding
    private lateinit var customerDao: CustomerDao
    private lateinit var rentalDao: RentalDao
    private lateinit var customer: Customer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActiveRentsStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rentalDao = AppDatabase.getDatabase(this).rentalDao
        customerDao = AppDatabase.getDatabase(this).customerDao
        customer = customerDao.getCustomerById(intent.getIntExtra(KEY_CUSTOMER_ID, 0))

        showData()

        setSupportActionBar(binding.toolbarFilm)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
        binding.toolbarFilm.title = "Rented film"
        val listRentedFilmOfCustomer: List<Rental> =
            rentalDao.listRentedFilmOfCustomer(customer.customer_id!!)

        binding.txtNumberActiveRents.text =
            "The number of rented film : " + listRentedFilmOfCustomer.size

        val adapter = RentedFilmAdapter(
            rents = ArrayList(listRentedFilmOfCustomer),
            this,
            false,
            AppDatabase.getDatabase(this)
        )
        binding.recycleActiveRentsStore.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycleActiveRentsStore.adapter = adapter

    }

    override fun onClickedItem(rental: Rental) {

    }


}