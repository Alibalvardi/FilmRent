package com.ali.filmrent.adapter

import com.ali.filmrent.dataClass.Rating
import com.ali.filmrent.dataClass.Rental
import com.ali.filmrent.dataClass.Reserve

interface RatingEvents {
    fun onClickedItem(rating: Rating)
}