package com.ali.filmrent.adapter

import com.ali.filmrent.dataClass.Film

interface FilmEvents {
    fun onClickedItem(film: Film)
}