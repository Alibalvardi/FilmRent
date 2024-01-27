package com.ali.filmrent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.activity.CustomerActiveRentActivity
import com.ali.filmrent.activity.KEY_CUSTOMER_ID
import com.ali.filmrent.activity.StoresOfFilmActivity
import com.ali.filmrent.adapter.FilmAdapter
import com.ali.filmrent.adapter.FilmEvents
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.databinding.FragmentFilmsCustomerBinding
import com.ali.filmrent.databinding.FragmentMyfilmCustomerBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.RentalDao

class FragmentFilmsCustomer : Fragment() , FilmEvents {

    private lateinit var binding: FragmentFilmsCustomerBinding
    private lateinit var filmDao : FilmDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilmsCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filmDao = AppDatabase.getDatabase(this.requireContext()).filmDao

        val allFilmList: List<Film> = filmDao.getAllFilms("")



        val adapter = FilmAdapter(
            films = ArrayList(allFilmList),
            -2,
            this,
            AppDatabase.getDatabase(this.requireContext())
        )
        binding.recycleMyFilmCustomer.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycleMyFilmCustomer.adapter = adapter

    }

    override fun onClickedItem(film: Film) {
        val intent = Intent(activity, StoresOfFilmActivity::class.java)
        intent.putExtra(KEY_FILM_ID,film.film_id)
        val customerId : Int = activity?.intent!!.getIntExtra(KEY_CUSTOMER_ID,0)
        intent.putExtra(KEY_CUSTOMER_ID , customerId)
        startActivity(intent)
    }
}