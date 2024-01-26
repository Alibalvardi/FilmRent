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
import com.ali.filmrent.adapter.PaymentAdapter
import com.ali.filmrent.adapter.PaymentEvents
import com.ali.filmrent.adapter.ReservationAdapter
import com.ali.filmrent.adapter.ReservationEvents
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Payment
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Reserve
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityAnswerToReservationBinding
import com.ali.filmrent.databinding.ReplyReserveDialogBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.PaymentDao
import com.ali.filmrent.roomDatabase.RentalDao
import com.ali.filmrent.roomDatabase.ReserveDao
import com.ali.filmrent.roomDatabase.StoreDao
import java.util.Calendar

class CustomerPaymentActivity : AppCompatActivity(), PaymentEvents {
    private lateinit var binding: ActivityAnswerToReservationBinding
    private lateinit var customerDao: CustomerDao
    private lateinit var paymentDao: PaymentDao
    private lateinit var customer: Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerToReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customerDao = AppDatabase.getDatabase(this).customerDao
        paymentDao = AppDatabase.getDatabase(this).paymentDao
        customer = customerDao.getCustomerById(intent.getIntExtra(KEY_CUSTOMER_ID, 0))

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
        binding.toolbarReserve.title = "All payments"
        val listPaymentOfCustomer: List<Payment> =
            paymentDao.listPaymentsOfCustomer(customer.customer_id!!)

        binding.txtNumberReserve.text =
            "The number of payment : " + listPaymentOfCustomer.size

        val adapter = PaymentAdapter(
            payments = ArrayList(listPaymentOfCustomer),
            itemEvents = this,
            false,
            AppDatabase.getDatabase(this)
        )
        binding.recycleReserve.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycleReserve.adapter = adapter

    }


    override fun onClickedItem(payment: Payment) {
    }
}