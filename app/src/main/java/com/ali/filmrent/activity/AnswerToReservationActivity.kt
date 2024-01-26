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
import com.ali.filmrent.adapter.ReservationAdapter
import com.ali.filmrent.adapter.ReservationEvents
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Reserve
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityAnswerToReservationBinding
import com.ali.filmrent.databinding.ReplyReserveDialogBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.RentalDao
import com.ali.filmrent.roomDatabase.ReserveDao
import com.ali.filmrent.roomDatabase.StoreDao
import java.util.Calendar

class AnswerToReservationActivity : AppCompatActivity(), ReservationEvents {
    private lateinit var binding: ActivityAnswerToReservationBinding
    private lateinit var storeDao: StoreDao
    private lateinit var reserveDao: ReserveDao
    private lateinit var boughtInventoryDao: BoughtInventoryDao
    private lateinit var filmDao: FilmDao
    private lateinit var rentalDao:RentalDao
    private lateinit var customerDao: CustomerDao
    private lateinit var store: Store

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnswerToReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeDao = AppDatabase.getDatabase(this).storeDao
        boughtInventoryDao = AppDatabase.getDatabase(this).boughtInventoryDao
        filmDao = AppDatabase.getDatabase(this).filmDao
        rentalDao = AppDatabase.getDatabase(this).rentalDao
        customerDao = AppDatabase.getDatabase(this).customerDao
        reserveDao = AppDatabase.getDatabase(this).reserveDao
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
        binding.toolbarReserve.title = "Reservation request"
        val listOfReservationsOfStore: List<Reserve> =
            reserveDao.listOfReservationsOfStore(store.store_id!!)

        binding.txtNumberReserve.text =
            "The number of Reservation: " + listOfReservationsOfStore.size

        val adapter = ReservationAdapter(
            reserves = ArrayList(listOfReservationsOfStore),
            itemEvents = this,
            true,
            AppDatabase.getDatabase(this)
        )
        binding.recycleReserve.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycleReserve.adapter = adapter

    }

    override fun onClickedItem(reserve: Reserve) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = ReplyReserveDialogBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        dialogBinding.btnRent.setOnClickListener {
            rentFilm(reserve)
        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnRemove.setOnClickListener {
            val sweetAlertDialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            sweetAlertDialog.titleText = "delete"
            sweetAlertDialog.confirmText = "Delete"
            sweetAlertDialog.cancelText = "cancel"
            sweetAlertDialog.contentText = "delete this reserve"
            sweetAlertDialog.setOnCancelListener {
                sweetAlertDialog.dismiss()
            }
            sweetAlertDialog.setConfirmClickListener {
                sweetAlertDialog.dismiss()
                reserveDao.removeReserve(reserve)
                showData()
            }
            sweetAlertDialog.show()

        }
    }

    private fun rentFilm(reserve: Reserve){
        val film = filmDao.getFilmById(reserve.film_id)
        val customer = customerDao.getCustomerById(reserve.customer_id)
        val cntAvlCopies: Int = boughtInventoryDao.countOfFilm(
            store.store_id!!,
            film.film_id!!
        ) - rentalDao.countOfActiveRentsOfFilm(store.store_id!!, film.film_id)
        if (cntAvlCopies > 0) {
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
                            customer_id = customer.customer_id,
                            film_id = film.film_id,
                            rentalDate = rentCalendar.timeInMillis,
                            returnDate = 0,
                            rentDuration = 14
                        )
                    )
                    reserveDao.removeReserve(reserve)
                    Toast.makeText(this, " Rented successfully", Toast.LENGTH_LONG).show()
                    showData()
                    dialog.dismiss()
                } else {
                    dialog.dismiss()
                    Toast.makeText(this, "this customer have 3 active rents from your store", Toast.LENGTH_LONG)
                        .show()
                }

            }
            dialog.show()
        } else {
            Toast.makeText(
                this,
                "There is no available copy.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}