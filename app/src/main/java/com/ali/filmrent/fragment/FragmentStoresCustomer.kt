package com.ali.filmrent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.activity.BuyFilmActivity
import com.ali.filmrent.activity.KEY_STORE_ID
import com.ali.filmrent.activity.StoreInformationForCustomerActivity
import com.ali.filmrent.adapter.FilmAdapter
import com.ali.filmrent.adapter.StoreAdapter
import com.ali.filmrent.adapter.StoreEvents
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.FragmentStoresCustomerBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.StoreDao

class FragmentStoresCustomer : Fragment(), StoreEvents {

    private lateinit var binding: FragmentStoresCustomerBinding
    private lateinit var storeDao: StoreDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoresCustomerBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storeDao = AppDatabase.getDatabase(this.requireContext()).storeDao


        val storeList: List<Store> = storeDao.listOfAllStore()
        val adapter =
            StoreAdapter(ArrayList(storeList), this, AppDatabase.getDatabase(this.requireContext()))
        binding.recycleStoresCustomer.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycleStoresCustomer.adapter = adapter
    }

    override fun onClickedItem(store: Store) {
        val intent = Intent(activity, StoreInformationForCustomerActivity::class.java)
        intent.putExtra(KEY_STORE_ID,store.store_id)
        startActivity(intent)
    }

    override fun onLongClickedItem(store: Store) {
    }

}