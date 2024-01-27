package com.ali.filmrent.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.databinding.ItemActiveRentBinding
import com.ali.filmrent.roomDatabase.AppDatabase

import java.text.SimpleDateFormat
import java.util.Calendar


class RentedFilmAdapter(
    private val rents: ArrayList<Rental>,
    val itemEvents: RentEvents,
    val isStore: Boolean,
    val appDatabase: AppDatabase
) :
    RecyclerView.Adapter<RentedFilmAdapter.ViewHolder>() {

    lateinit var binding: ItemActiveRentBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindViews(rent: Rental) {
            val customer = appDatabase.customerDao.getCustomerById(rent.customer_id)
            val store = appDatabase.storeDao.getStoreById(rent.store_id)
            val film = appDatabase.filmDao.getFilmById(rent.film_id)

            binding.txtFilmTitle.text = film.title

            if (!isStore) {
                binding.txtName.text = "Store : " + store.name
                binding.txtPhone.text = "Phone : " + store.phoneNumber
            } else {
                binding.txtName.text = "Customer : " + customer.firstname + " " + customer.lastname
                binding.txtPhone.text = "Phone : " + store.phoneNumber
                binding.txtReturn.visibility = TextView.INVISIBLE
            }

            val startDate = Calendar.getInstance()
            startDate.timeInMillis = rent.rentalDate
            binding.txtStartDate.text = "Rent Start Date : " + changeCalendarToString(startDate)

            val returnDate = Calendar.getInstance()
            returnDate.timeInMillis = rent.returnDate
            binding.txtReturnDate.text = "Return Date : " + changeCalendarToString(returnDate)

            val duration: Int =
                ((returnDate.timeInMillis - startDate.timeInMillis) / 86400000).toInt() + 1
            if (duration > rent.rentDuration) {
                binding.txtLateDays.setTextColor(Color.parseColor("#FFF50000"))
                binding.txtLateDays.text =
                    "Rental duration : $duration day"
            } else {
                binding.txtLateDays.setTextColor(Color.parseColor("#12B31A"))
                binding.txtLateDays.text =
                    "Rental duration : $duration day"
            }


            if (duration > rent.rentDuration) {
                val late: Int =
                    (duration / rent.rentDuration) * (duration % rent.rentDuration)
                binding.txtPayment.text =
                    "Payment amount : $" + ((rent.rentDuration * 2) + (late * 3))
            } else {
                binding.txtPayment.text =
                    "Payment amount : $" + (duration * 2)
            }


            binding.txtRentNumber.text = (adapterPosition + 1).toString()
            itemView.setOnClickListener {
                itemEvents.onClickedItem(rent)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemActiveRentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(rents[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return rents.size
    }

    @SuppressLint("SimpleDateFormat")
    private fun changeCalendarToString(myCalendar: Calendar): String {
        return SimpleDateFormat("dd/MM/yyyy").format(myCalendar.time)
    }

}