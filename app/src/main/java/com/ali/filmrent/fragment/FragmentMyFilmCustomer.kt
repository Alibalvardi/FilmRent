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
import com.ali.filmrent.activity.KEY_STORE_ID
import com.ali.filmrent.activity.RentActivity
import com.ali.filmrent.activity.StoreInformationForCustomerActivity
import com.ali.filmrent.adapter.FilmAdapter
import com.ali.filmrent.adapter.FilmEvents
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.databinding.FragmentMyfilmCustomerBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.ali.filmrent.roomDatabase.RentalDao

class FragmentMyFilmCustomer : Fragment() , FilmEvents {

    private lateinit var binding: FragmentMyfilmCustomerBinding
    private lateinit var rentalDao: RentalDao
    private lateinit var filmDao : FilmDao
    private lateinit var customerDao: CustomerDao
    private lateinit var customer: Customer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyfilmCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rentalDao = AppDatabase.getDatabase(this.requireContext()).rentalDao
        filmDao = AppDatabase.getDatabase(this.requireContext()).filmDao
        customerDao = AppDatabase.getDatabase(this.requireContext()).customerDao
        val customerId: Int = activity?.intent!!.getIntExtra(KEY_CUSTOMER_ID, 0)
        customer = customerDao.getCustomerById(customerId)

        val customeractiveFilmsId: List<Int> = rentalDao.getCustomerActiveFilms(customer.customer_id!!)
        val customerFilms = filmDao.getFilmsById(customeractiveFilmsId)


        val adapter = FilmAdapter(
            films = ArrayList(customerFilms),
            -1,
            this,
            AppDatabase.getDatabase(this.requireContext())
        )
        binding.recycleMyFilmCustomer.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycleMyFilmCustomer.adapter = adapter

    }

    override fun onClickedItem(film: Film) {
        val intent = Intent(activity, CustomerActiveRentActivity::class.java)
        intent.putExtra(KEY_FILM_ID,film.film_id)
        val customerId : Int = activity?.intent!!.getIntExtra(KEY_CUSTOMER_ID,0)
        intent.putExtra(KEY_CUSTOMER_ID , customerId)
        startActivity(intent)
    }

}