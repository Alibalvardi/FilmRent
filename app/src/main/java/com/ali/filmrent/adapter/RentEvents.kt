package com.ali.filmrent.adapter

import com.ali.filmrent.dataClass.Rental

interface RentEvents {
    fun onClickedItem(rental: Rental)
}