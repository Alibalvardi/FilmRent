package com.ali.filmrent.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.dataClass.Reserve
import com.ali.filmrent.databinding.ItemActiveRentBinding
import com.ali.filmrent.databinding.ItemReserveBinding
import com.ali.filmrent.roomDatabase.AppDatabase

import java.text.SimpleDateFormat
import java.util.Calendar


class ReservationAdapter(
    private val reserves: ArrayList<Reserve>,
    val itemEvents: ReservationEvents,
    val isStore: Boolean,
    val appDatabase: AppDatabase
) :
    RecyclerView.Adapter<ReservationAdapter.ViewHolder>() {

    lateinit var binding: ItemReserveBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindViews(reserve: Reserve) {
            val customer = appDatabase.customerDao.getCustomerById(reserve.customer_id)
            val store = appDatabase.storeDao.getStoreById(reserve.store_id)
            val film = appDatabase.filmDao.getFilmById(reserve.film_id)

            binding.txtFilmTitle.text = film.title

            if (isStore) {
                binding.txtName.text = "Customer : " + customer.firstname + " " + customer.lastname
                binding.txtPhone.text = "Phone : " + store.phoneNumber
            } else {
                binding.txtName.text = "Store : " + store.name
                binding.txtPhone.text = "Phone : " + store.phoneNumber
                binding.txtInf.text = "Top to remove"
                binding.txtInf.setTextColor(Color.parseColor("#FFF50000"))
            }

            val reserveDate = Calendar.getInstance()
            reserveDate.timeInMillis = reserve.reserveDate
            binding.txtReserveDate.text =
                "Reservation Date : " + changeCalendarToString(reserveDate)


            binding.txtReserveNumber.text = (adapterPosition + 1).toString()
            itemView.setOnClickListener {
                itemEvents.onClickedItem(reserve)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemReserveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(reserves[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return reserves.size
    }

    @SuppressLint("SimpleDateFormat")
    private fun changeCalendarToString(myCalendar: Calendar): String {
        return SimpleDateFormat("dd/MM/yyyy").format(myCalendar.time)
    }

}