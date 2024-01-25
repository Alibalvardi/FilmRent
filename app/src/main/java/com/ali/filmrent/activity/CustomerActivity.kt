package com.ali.filmrent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ali.filmrent.R
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.databinding.ActivityCustomerBinding
import com.ali.filmrent.fragment.FragmentFilmsCustomer
import com.ali.filmrent.fragment.FragmentMyFilmCustomer
import com.ali.filmrent.fragment.FragmentProfileCustomer
import com.ali.filmrent.fragment.FragmentStoresCustomer
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CustomerDao
import com.google.android.material.navigation.NavigationBarView

class CustomerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerBinding
    private lateinit var customerDao: CustomerDao
    private lateinit var customer: Customer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //get data From database
        customerDao = AppDatabase.getDatabase(this).customerDao
        val customer_id: Int = intent.getIntExtra(KEY_CUSTOMER_ID, 0)
        customer = customerDao.getCustomerById(customer_id)

        binding.toolBarCustomer.title = customer.firstname + "  " + customer.lastname
        binding.bottomNavigationCustomer.labelVisibilityMode =
            NavigationBarView.LABEL_VISIBILITY_LABELED


        setSupportActionBar(binding.toolBarCustomer)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayoutCustomer,
            binding.toolBarCustomer,
            R.string.open_drawer,
            R.string.close_drawer
        )
        actionBarDrawerToggle.syncState()
        firstRun()
        binding.drawerLayoutCustomer.addDrawerListener(actionBarDrawerToggle)

        binding.bottomNavigationCustomer.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_myFilm_customer -> {
                    replaceFragment(FragmentMyFilmCustomer())
                }

                R.id.menu_stores_customer -> {
                    replaceFragment(FragmentStoresCustomer())
                }

                R.id.menu_films_customer -> {
                    replaceFragment(FragmentFilmsCustomer())
                }

                R.id.menu_profile_customer -> {
                    replaceFragment(FragmentProfileCustomer())
                }
            }
            true
        }
        binding.bottomNavigationCustomer.setOnItemReselectedListener {}
    }

    private fun firstRun() {

        replaceFragment(FragmentMyFilmCustomer())
        binding.bottomNavigationCustomer.selectedItemId = R.id.menu_myFilm

    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_customer_container, fragment)
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

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }
}