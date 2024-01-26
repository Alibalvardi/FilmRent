package com.ali.filmrent.adapter

import com.ali.filmrent.dataClass.Payment
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Reserve

interface PaymentEvents {
    fun onClickedItem(payment: Payment)
}