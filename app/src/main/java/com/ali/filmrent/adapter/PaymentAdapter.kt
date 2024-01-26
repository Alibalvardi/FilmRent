package com.ali.filmrent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.dataClass.Payment
import com.ali.filmrent.databinding.ItemPaymentBinding
import com.ali.filmrent.roomDatabase.AppDatabase

import java.text.SimpleDateFormat
import java.util.Calendar


class PaymentAdapter(
    private val payments: ArrayList<Payment>,
    val itemEvents: PaymentEvents,
    val isStore: Boolean,
    val appDatabase: AppDatabase
) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {

    lateinit var binding: ItemPaymentBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindViews(payment: Payment) {
            val customer = appDatabase.customerDao.getCustomerById(payment.customer_id)
            val store = appDatabase.storeDao.getStoreById(payment.store_id)
            val film =
                appDatabase.filmDao.getFilmById(appDatabase.rentalDao.getRentById(payment.rental_id).film_id)


            if (payment.customer_id != 0) {
                binding.txtFilmTitle.text = "Film : " + film.title
                if (isStore) {
                    binding.txtName.text =
                        "Customer : " + customer.firstname + " " + customer.lastname
                    binding.txtInf.text = "Receive"
                    binding.txtPaymentAmount.text = "Receive $${payment.amount} for rent"
                } else {
                    binding.txtName.text = "Store : " + store.name
                    binding.txtInf.text = "Paying"
                    binding.txtPaymentAmount.text = "Paying $${payment.amount} for rent"
                }
            } else {
                binding.txtFilmTitle.text =
                    "Film : " + appDatabase.filmDao.getFilmById(payment.rental_id).title
                binding.txtName.text = "Buy film "
                binding.txtInf.text = "Paying"
                binding.txtPaymentAmount.text = "Paying $${payment.amount} for buy film"
            }
            val paymentDate = Calendar.getInstance()
            paymentDate.timeInMillis = payment.settlementDate
            binding.txtPaymentDate.text =
                "Payment Date : " + changeCalendarToString(paymentDate)


            binding.txtReserveNumber.text = (adapterPosition + 1).toString()
            itemView.setOnClickListener {
                itemEvents.onClickedItem(payment)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(payments[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    @SuppressLint("SimpleDateFormat")
    private fun changeCalendarToString(myCalendar: Calendar): String {
        return SimpleDateFormat("dd/MM/yyyy").format(myCalendar.time)
    }

}