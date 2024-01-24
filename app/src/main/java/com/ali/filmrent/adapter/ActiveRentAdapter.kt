package com.ali.filmrent.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ItemActiveRentBinding
import com.ali.filmrent.databinding.ItemStoreBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import java.text.SimpleDateFormat
import java.util.Calendar


class ActiveRentAdapter(
    private val rents: ArrayList<Rental>,
    val itemEvents: RentEvents,
    val bool: Boolean,
    val appDatabase: AppDatabase
) :
    RecyclerView.Adapter<ActiveRentAdapter.ViewHolder>() {

    lateinit var binding: ItemActiveRentBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindViews(rent: Rental) {
            val customer = appDatabase.customerDao.getCustomerById(rent.customer_id)
            val store = appDatabase.storeDao.getStoreById(rent.store_id)
            val film = appDatabase.filmDao.getFilmById(rent.film_id)

            binding.txtFilmTitle.text = film.title

            if (bool) {
                binding.txtName.text = "Store : " + store.name
                binding.txtPhone.text = "Phone : " + store.phoneNumber
            } else {
                binding.txtName.text = "Customer : " + customer.firstname + " " + customer.lastname
                binding.txtPhone.text = "Phone : " + store.phoneNumber
            }

            val startDate = Calendar.getInstance()
            startDate.timeInMillis = rent.rentalDate
            binding.txtStartDate.text = "Rent Start Date : " + changeCalendarToString(startDate)

            val returnDate = Calendar.getInstance()
            returnDate.timeInMillis=rent.rentalDate
            returnDate.add(Calendar.DATE, rent.rentDuration-1)
            binding.txtReturnDate.text = "Return deadline : " + changeCalendarToString(returnDate)

            val nowDate = Calendar.getInstance()
            nowDate.timeInMillis = appDatabase.calendarDao.getCalendar(1)

            if (nowDate.after(returnDate)) {
                binding.txtLateDays.setTextColor(Color.parseColor("#FFF50000"))
                binding.txtLateDays.text =
                    "Late days : " + ((nowDate.timeInMillis - returnDate.timeInMillis) / 86400000).toInt() + " day"
            } else {
                binding.txtLateDays.setTextColor(Color.parseColor("#12B31A"))
                binding.txtLateDays.text =
                    "remaining day : " + ((returnDate.timeInMillis - nowDate.timeInMillis) / 86400000).toInt() + " day"
            }

            val nowDuration: Int =
                ((nowDate.timeInMillis - startDate.timeInMillis) / 86400000).toInt() + 1
            if (nowDuration > rent.rentDuration) {
                val late : Int = (nowDuration/rent.rentDuration)*(nowDuration%rent.rentDuration)
                binding.txtPayment.text =
                    "The amount to be paid now : $" + ((rent.rentDuration *2)+(late *3))
            } else {
                binding.txtPayment.text =
                    "The amount to be paid now : $" + (nowDuration * 2)
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