package com.ali.filmrent.adapter

import com.ali.filmrent.dataClass.Store

interface StoreEvents {
    fun onClickedItem(store: Store)

    fun onLongClickedItem(store: Store)

}