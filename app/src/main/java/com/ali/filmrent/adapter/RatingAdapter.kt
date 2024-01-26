package com.ali.filmrent.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.dataClass.Rating
import com.ali.filmrent.dataClass.Reserve
import com.ali.filmrent.databinding.ItemActiveRentBinding
import com.ali.filmrent.databinding.ItemRatingBinding
import com.ali.filmrent.databinding.ItemReserveBinding
import com.ali.filmrent.roomDatabase.AppDatabase

import java.text.SimpleDateFormat
import java.util.Calendar


class RatingAdapter(
    private val rates: ArrayList<Rating>,
    val itemEvents: RatingEvents,
    val isStore: Boolean,
    val appDatabase: AppDatabase
) :
    RecyclerView.Adapter<RatingAdapter.ViewHolder>() {

    lateinit var binding: ItemRatingBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindViews(rating: Rating) {
            val customer = appDatabase.customerDao.getCustomerById(rating.customer_id)
            val store = appDatabase.storeDao.getStoreById(rating.store_id)

            if (isStore) {
                binding.txtName.text = "Customer :  " + customer.firstname + " " + customer.lastname
            } else {
                binding.txtName.text = "Store :  " + store.name
            }
            binding.itemRatingbar.rating = rating.rating
            binding.txtRatingNumber.text = (adapterPosition + 1).toString()

            itemView.setOnClickListener {
                itemEvents.onClickedItem(rating)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(rates[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return rates.size
    }


}