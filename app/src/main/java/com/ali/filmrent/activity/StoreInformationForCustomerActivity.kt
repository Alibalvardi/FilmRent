package com.ali.filmrent.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.adapter.FilmAdapter
import com.ali.filmrent.adapter.FilmEvents
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityStoreInformationForCustomerBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.StoreDao
import com.bumptech.glide.Glide

class StoreInformationForCustomerActivity : AppCompatActivity(), FilmEvents {
    private lateinit var binding: ActivityStoreInformationForCustomerBinding
    private lateinit var store: Store
    private lateinit var storeDao: StoreDao
    private lateinit var filmDao: FilmDao
    private lateinit var boughtInventoryDao: BoughtInventoryDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreInformationForCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeDao = AppDatabase.getDatabase(this).storeDao
        boughtInventoryDao = AppDatabase.getDatabase(this).boughtInventoryDao
        filmDao = AppDatabase.getDatabase(this).filmDao
        val store_id: Int = intent.getIntExtra(KEY_STORE_ID, 0)
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


    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
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
            1,
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

    }
}