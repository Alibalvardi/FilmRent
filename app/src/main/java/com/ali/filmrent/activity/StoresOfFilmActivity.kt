package com.ali.filmrent.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.R
import com.ali.filmrent.adapter.ActiveRentAdapter
import com.ali.filmrent.adapter.StoreAdapter
import com.ali.filmrent.adapter.StoreEvents
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityCustomerActiveRentBinding
import com.ali.filmrent.databinding.ActivityStoresOfFilmBinding
import com.ali.filmrent.fragment.KEY_FILM_ID
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.StoreDao
import com.bumptech.glide.Glide

class StoresOfFilmActivity : AppCompatActivity(), StoreEvents {
    private lateinit var binding: ActivityStoresOfFilmBinding
    private lateinit var customer: Customer
    private lateinit var film: Film
    private lateinit var filmDao: FilmDao
    private lateinit var customerDao: CustomerDao
    private lateinit var boughtInventoryDao: BoughtInventoryDao
    private lateinit var storeDao: StoreDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoresOfFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)


        boughtInventoryDao = AppDatabase.getDatabase(this).boughtInventoryDao
        filmDao = AppDatabase.getDatabase(this).filmDao
        customerDao = AppDatabase.getDatabase(this).customerDao
        storeDao = AppDatabase.getDatabase(this).storeDao
        val film_id: Int = intent.getIntExtra(KEY_FILM_ID, 0)
        val customer_id: Int = intent.getIntExtra(KEY_CUSTOMER_ID, 0)
        customer = customerDao.getCustomerById(customer_id)
        film = filmDao.getFilmById(film_id)

        showData()

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

        val storesOfFilmId: List<Int> = boughtInventoryDao.storesOfFilm(film.film_id!!)
        val storeList: List<Store> = storeDao.getListOfStore(storesOfFilmId)
        binding.txtNumberStore.text = "Number of store : " + storeList.size
        val adapter =
            StoreAdapter(ArrayList(storeList), film.film_id!!, this, AppDatabase.getDatabase(this))
        binding.recycleStore.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycleStore.adapter = adapter


    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    override fun onClickedItem(store: Store) {
        val intent = Intent(this, RentActivity::class.java)
        intent.putExtra(KEY_STORE_ID, store.store_id)
        intent.putExtra(KEY_CUSTOMER_ID, customer.customer_id)
        intent.putExtra(KEY_FILM_ID, film.film_id)
        startActivity(intent)
    }

    override fun onLongClickedItem(store: Store) {
    }
}