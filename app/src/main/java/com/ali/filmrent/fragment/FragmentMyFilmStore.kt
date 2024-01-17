package com.ali.filmrent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.activity.KEY_STORE_ID
import com.ali.filmrent.activity.StoreFilmInformationActivity
import com.ali.filmrent.adapter.FilmAdapter
import com.ali.filmrent.adapter.FilmEvents
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.databinding.FragmentMyFilmStoreBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.FilmDao

class FragmentMyFilmStore : Fragment(), FilmEvents {

    private lateinit var binding: FragmentMyFilmStoreBinding
    private lateinit var boughtInventoryDao: BoughtInventoryDao
    private lateinit var filmDao: FilmDao
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyFilmStoreBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boughtInventoryDao = AppDatabase.getDatabase(this.requireContext()).boughtInventoryDao
        filmDao = AppDatabase.getDatabase(this.requireContext()).filmDao
        val store_id: Int = activity?.intent!!.getIntExtra(KEY_STORE_ID, 0)


        val storeFilmsId: List<Int> = boughtInventoryDao.getStoreFilms(store_id)
        val storeFilms = filmDao.getFilmsById(storeFilmsId)


        val adapter = FilmAdapter(
            films = ArrayList(storeFilms),
            store_id,
            this,
            AppDatabase.getDatabase(this.requireContext())
        )
        binding.recycleMyFilmStore.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycleMyFilmStore.adapter = adapter

    }


    override fun onClickedItem(film: Film) {
        val intent = Intent(activity, StoreFilmInformationActivity::class.java)
        intent.putExtra(KEY_FILM_ID, film.film_id)
        val store_id: Int = activity?.intent!!.getIntExtra(KEY_STORE_ID, 0)
        intent.putExtra(KEY_STORE_ID, store_id)
        startActivity(intent)
    }
}