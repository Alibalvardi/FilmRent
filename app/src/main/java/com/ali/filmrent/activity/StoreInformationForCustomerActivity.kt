package com.ali.filmrent.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.adapter.FilmAdapter
import com.ali.filmrent.adapter.FilmEvents
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.dataClass.Rating
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityStoreInformationForCustomerBinding
import com.ali.filmrent.databinding.LoginDialogBinding
import com.ali.filmrent.databinding.RatingToStoreDialogBinding
import com.ali.filmrent.fragment.KEY_FILM_ID
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.RatingDao
import com.ali.filmrent.roomDatabase.StoreDao
import com.bumptech.glide.Glide

class StoreInformationForCustomerActivity : AppCompatActivity(), FilmEvents {
    private lateinit var binding: ActivityStoreInformationForCustomerBinding
    private lateinit var store: Store
    private lateinit var customer: Customer
    private lateinit var storeDao: StoreDao
    private lateinit var filmDao: FilmDao
    private lateinit var boughtInventoryDao: BoughtInventoryDao
    private lateinit var ratingDao: RatingDao
    private lateinit var customerDao: CustomerDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreInformationForCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set start information
        storeDao = AppDatabase.getDatabase(this).storeDao
        boughtInventoryDao = AppDatabase.getDatabase(this).boughtInventoryDao
        filmDao = AppDatabase.getDatabase(this).filmDao
        ratingDao = AppDatabase.getDatabase(this).ratingDao
        customerDao = AppDatabase.getDatabase(this).customerDao
        val store_id: Int = intent.getIntExtra(KEY_STORE_ID, 0)
        val customer_id: Int = intent.getIntExtra(KEY_CUSTOMER_ID, 0)
        customer = customerDao.getCustomerById(customer_id)
        store = storeDao.getStoreById(store_id)


        setSupportActionBar(binding.toolBarStore)
        binding.collapsingStore.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        showData()

        binding.fabButtonRate.setOnClickListener {
            rateToStore()
        }


    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun rateToStore() {

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
                ratingDao.getRateIfRated(store.store_id!!, customer.customer_id!!)
        }

        dialogBinding.btnDone.setOnClickListener {


            if (ratingDao.ifCustomerRatedStore(store.store_id!!, customer.customer_id!!) == 0) {
                ratingDao.insertRating(
                    Rating(
                        rating = dialogBinding.ratingBar.rating,
                        store_id = store.store_id!!,
                        customer_id = customer.customer_id!!
                    )
                )
                Toast.makeText(this, "your rate was submit", Toast.LENGTH_SHORT).show()
            } else {
                ratingDao.updateRating(
                    dialogBinding.ratingBar.rating,
                    store_id = store.store_id!!,
                    customer_id = customer.customer_id!!
                )
                Toast.makeText(this, "your rate was updated", Toast.LENGTH_SHORT).show()
            }

            val storeRate: Float = ratingDao.avgStoreRating(store.store_id!!)
            storeDao.updateStoreRate(store.store_id!!, storeRate)

            showData()
            dialog.dismiss()
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
        val store_id: Int = intent.getIntExtra(KEY_STORE_ID, 0)
        store = storeDao.getStoreById(store_id)
        val manager = AppDatabase.getDatabase(this).managerDao.returnManagerById(store.manager_id)
        binding.collapsingStore.title = store.name + "   Store"
        Glide.with(this).load(store.url).into(binding.imgStoreProfile)
        binding.txtStoreName.text = "Store name : " + store.name
        binding.txtStorePhone.text = "Phone : " + store.phoneNumber
        binding.txtStoreManagerName.text = "Manager : " + manager.firstname + " " + manager.lastname
        binding.txtManagerPhone.text = "Manager phone : " + manager.phoneNumber
        binding.itemRatingBarStore.rating = store.rating

        val storeFilmsId: List<Int> = boughtInventoryDao.getStoreFilms(store.store_id!!)
        val storeFilms = filmDao.getFilmsById(storeFilmsId)
        val adapter = FilmAdapter(
            films = ArrayList(storeFilms),
            store.store_id!!,
            this,
            AppDatabase.getDatabase(this)
        )
        binding.recycleStoreFilms.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycleStoreFilms.adapter = adapter


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    override fun onClickedItem(film: Film) {
        val intent = Intent(this, RentActivity::class.java)
        intent.putExtra(KEY_STORE_ID, store.store_id)
        intent.putExtra(KEY_CUSTOMER_ID, customer.customer_id)
        intent.putExtra(KEY_FILM_ID, film.film_id)
        startActivity(intent)
    }
}