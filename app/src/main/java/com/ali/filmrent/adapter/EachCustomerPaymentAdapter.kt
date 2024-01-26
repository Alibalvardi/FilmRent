package com.ali.filmrent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.dataClass.Payment
import com.ali.filmrent.databinding.ItemCustomerPaymentBinding
import com.ali.filmrent.databinding.ItemPaymentBinding
import com.ali.filmrent.roomDatabase.AppDatabase

import java.text.SimpleDateFormat
import java.util.Calendar


class EachCustomerPaymentAdapter(
    private val payments: ArrayList<Payment>,
    val itemEvents: PaymentEvents,
    val isStore: Boolean,
    val appDatabase: AppDatabase
) :
    RecyclerView.Adapter<EachCustomerPaymentAdapter.ViewHolder>() {

    lateinit var binding: ItemCustomerPaymentBinding

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindViews(payment: Payment) {
            val customer = appDatabase.customerDao.getCustomerById(payment.customer_id)

            binding.txtName.text =
                "Customer : " + customer.firstname + " " + customer.lastname
            binding.txtPaymentAmount.text =
                "Total payment for this customer : $" + appDatabase.paymentDao.sumOfPaymentOfEachCustomer(
                    payment.customer_id,
                    payment.store_id
                )

            binding.txtReserveNumber.text = (adapterPosition + 1).toString()
            itemView.setOnClickListener {
                itemEvents.onClickedItem(payment)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ItemCustomerPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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