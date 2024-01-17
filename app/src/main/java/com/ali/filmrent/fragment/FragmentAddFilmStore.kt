package com.ali.filmrent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.activity.BuyFilmActivity
import com.ali.filmrent.activity.KEY_STORE_ID
import com.ali.filmrent.adapter.FilmAdapter
import com.ali.filmrent.adapter.FilmEvents
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.databinding.FragmentAddFilmStoreBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.FilmDao

const val KEY_FILM_ID = "key_film_id"

class FragmentAddFilmStore : Fragment(), FilmEvents {

    private lateinit var binding: FragmentAddFilmStoreBinding
    private lateinit var filmDao: FilmDao
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddFilmStoreBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmDao = AppDatabase.getDatabase(this.requireContext()).filmDao





        val filmList = filmDao.getAllFilms()
        val adapter = FilmAdapter(films = ArrayList(filmList), this)
        binding.recycleAddFilmStore.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycleAddFilmStore.adapter = adapter
    }


    override fun onClickedItem(film: Film) {
        val intent = Intent(activity, BuyFilmActivity::class.java)
        intent.putExtra(KEY_FILM_ID, film.film_id)
        val store_id : Int = activity?.intent!!.getIntExtra(KEY_STORE_ID,0)
        intent.putExtra(KEY_STORE_ID,store_id)
        startActivity(intent)
    }
}