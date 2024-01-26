package com.ali.filmrent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ali.filmrent.R
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityStoreBinding
import com.ali.filmrent.fragment.FragmentAddFilmStore
import com.ali.filmrent.fragment.FragmentMyFilmStore
import com.ali.filmrent.fragment.FragmentProfileStore
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.StoreDao

class StoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoreBinding
    private lateinit var store: Store
    private lateinit var storeDao: StoreDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get information from database
        storeDao = AppDatabase.getDatabase(this).storeDao
        val store_id: Int = intent.getIntExtra(KEY_STORE_ID, 0)
        store = storeDao.getStoreById(store_id)



        binding.toolBarStore.title = store.name + "  Store"
        setSupportActionBar(binding.toolBarStore)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayoutStore,
            binding.toolBarStore,
            R.string.open_drawer,
            R.string.close_drawer
        )
        actionBarDrawerToggle.syncState()
        firstRun()
        binding.drawerLayoutStore.addDrawerListener(actionBarDrawerToggle)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_myFilm -> {
                    replaceFragment(FragmentMyFilmStore())
                }

                R.id.menu_add_film -> {
                    replaceFragment(FragmentAddFilmStore())
                }

                R.id.menu_profile_store -> {
                    replaceFragment(FragmentProfileStore())
                }
            }
            true
        }
        binding.bottomNavigation.setOnItemReselectedListener {}

        binding.navigationDrawerStore.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.menu_active_rents -> {
                    val intent = Intent(this, ActiveRentsStoreActivity::class.java)
                    intent.putExtra(KEY_STORE_ID, store.store_id)
                    startActivity(intent)
                }

                R.id.menu_reservation_request -> {
                    val intent = Intent(this, AnswerToReservationActivity::class.java)
                    intent.putExtra(KEY_STORE_ID, store.store_id)
                    startActivity(intent)
                }
                R.id.menu_store_rating -> {
                    val intent = Intent(this, RatingOfStoreActivity::class.java)
                    intent.putExtra(KEY_STORE_ID, store.store_id)
                    startActivity(intent)
                }
                R.id.menu_payment_store -> {
                    val intent = Intent(this, StorePaymentActivity::class.java)
                    intent.putExtra(KEY_STORE_ID, store.store_id)
                    startActivity(intent)
                }
            }
            true
        }

    }

    private fun firstRun() {

        replaceFragment(FragmentMyFilmStore())
        binding.bottomNavigation.selectedItemId = R.id.menu_myFilm

    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_store_container, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_exit -> {
                val dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                dialog.titleText = "log out !"
                dialog.confirmText = "confirm"
                dialog.cancelText = "cancel"
                dialog.contentText = "log out?"
                dialog.setOnCancelListener {
                    dialog.dismiss()
                }
                dialog.setConfirmClickListener {
                    dialog.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
                dialog.show()

            }
        }
        return true
    }

}