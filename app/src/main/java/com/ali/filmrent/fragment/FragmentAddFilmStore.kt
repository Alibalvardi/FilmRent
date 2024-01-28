package com.ali.filmrent.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
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
import com.ali.filmrent.roomDatabase.CategoryDao
import com.ali.filmrent.roomDatabase.FilmDao
import kotlin.properties.Delegates

const val KEY_FILM_ID = "key_film_id"

class FragmentAddFilmStore : Fragment(), FilmEvents {

    private lateinit var binding: FragmentAddFilmStoreBinding
    private lateinit var filmDao: FilmDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var category: String
    private lateinit var search: String
    private var topRate: Boolean = false
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
        categoryDao = AppDatabase.getDatabase(this.requireContext()).categoryDao
        category = ""
        search = ""

        showData(filmDao.getAllFilms(category))

        val categoryList: ArrayList<String> = ArrayList(categoryDao.getAllCategory())
        categoryList.add(0, "all category")
        val categoryAdapter: ArrayAdapter<String> = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categoryList
        )
        binding.edtCategory.setAdapter(categoryAdapter)


        binding.edtCategory.setOnItemClickListener { adapterView, _, position, _ ->
            category = adapterView.getItemAtPosition(position).toString()
            setCategory()
        }

        val itemSearch = listOf("Title", "Actor", "Language", "Year of release")
        val searchAdapter: ArrayAdapter<String> = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            itemSearch
        )
        binding.edtSearch.setAdapter(searchAdapter)


        binding.edtSearch.setOnItemClickListener { adapterView, _, position, _ ->
            search = adapterView.getItemAtPosition(position).toString()
            binding.edtSearch.setText("")
            binding.textInputSearch.hint = "search $search ..."
        }


        binding.edtSearch.addTextChangedListener { edtText ->
            searchOnDatabase(edtText!!.toString())
        }

        binding.switchTopRate.setOnCheckedChangeListener { _, isChecked ->
            topRate = isChecked
            setCategory()
        }

    }

    private fun setCategory() {
        if (topRate) {
            if (category == "all category")
                category = ""
            showData(filmDao.getAllFilmsByTopRate(category))
        } else {
            if (category == "all category")
                category = ""
            showData(filmDao.getAllFilms(category))
        }
    }

    private fun searchOnDatabase(searching: String) {
        when (search) {
            "Title" -> {
                if (searching.isNotEmpty()) {
                    showData(filmDao.searchTitleFilms(searching, category))
                } else {
                    showData(filmDao.getAllFilms(category))
                }
            }

            "Actor" -> {
                if (searching.isNotEmpty()) {
                    showData(filmDao.searchActorFilms(searching, category))
                } else {
                    showData(filmDao.getAllFilms(category))
                }
            }

            "Language" -> {
                if (searching.isNotEmpty()) {
                    showData(filmDao.searchLanguageFilms(searching, category))
                } else {
                    showData(filmDao.getAllFilms(category))
                }
            }

            "Year of release" -> {
                if (searching.isNotEmpty()) {
                    showData(filmDao.searchYearFilms(searching, category))
                } else {
                    showData(filmDao.getAllFilms(category))
                }
            }
        }
    }

    private fun showData(filmList: List<Film>) {
        val adapter = FilmAdapter(
            films = ArrayList(filmList),
            0,
            this,
            AppDatabase.getDatabase(this.requireContext())
        )
        binding.recycleAddFilmStore.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recycleAddFilmStore.adapter = adapter
    }


    override fun onClickedItem(film: Film) {
        val intent = Intent(activity, BuyFilmActivity::class.java)
        intent.putExtra(KEY_FILM_ID, film.film_id)
        val store_id: Int = activity?.intent!!.getIntExtra(KEY_STORE_ID, 0)
        intent.putExtra(KEY_STORE_ID, store_id)
        startActivity(intent)
    }
}