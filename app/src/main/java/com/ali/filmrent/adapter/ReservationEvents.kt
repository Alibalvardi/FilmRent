package com.ali.filmrent.adapter

import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Reserve

interface ReservationEvents {
    fun onClickedItem(reserve: Reserve)
}