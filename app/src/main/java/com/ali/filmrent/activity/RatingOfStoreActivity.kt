package com.ali.filmrent.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ali.filmrent.adapter.RatingAdapter
import com.ali.filmrent.adapter.RatingEvents
import com.ali.filmrent.adapter.ReservationAdapter
import com.ali.filmrent.adapter.ReservationEvents
import com.ali.filmrent.dataClass.Rating
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Reserve
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityAnswerToReservationBinding
import com.ali.filmrent.databinding.ReplyReserveDialogBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.RatingDao
import com.ali.filmrent.roomDatabase.RentalDao
import com.ali.filmrent.roomDatabase.ReserveDao
import com.ali.filmrent.roomDatabase.StoreDao
import java.util.Calendar

class RatingOfStoreActivity : AppCompatActivity(), RatingEvents {
    private lateinit var binding: ActivityAnswerToReservationBinding
    private lateinit var storeDao: StoreDao
    private lateinit var ratingDao: RatingDao
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerToReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeDao = AppDatabase.getDatabase(this).storeDao
        ratingDao = AppDatabase.getDatabase(this).ratingDao
        store = storeDao.getStoreById(intent.getIntExtra(KEY_STORE_ID, 0))

        showData()

        setSupportActionBar(binding.toolbarReserve)
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
        binding.toolbarReserve.title = "Rating of each customer"
        val listOfRatingOfStore: List<Rating> =
            ratingDao.listOfRatingOfStore(store.store_id!!)

        binding.txtNumberReserve.text =
            "The number of Rating : " + listOfRatingOfStore.size

        val adapter = RatingAdapter(
            rates = ArrayList(listOfRatingOfStore),
            itemEvents = this,
            true,
            AppDatabase.getDatabase(this)
        )
        binding.recycleReserve.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycleReserve.adapter = adapter
    }



    override fun onClickedItem(rating: Rating) {
    }
}