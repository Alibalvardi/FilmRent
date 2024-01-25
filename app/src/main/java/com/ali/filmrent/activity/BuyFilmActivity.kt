package com.ali.filmrent.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ali.filmrent.dataClass.BoughtInventory
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.databinding.ActivityBuyFilmBinding
import com.ali.filmrent.fragment.KEY_FILM_ID
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.BoughtInventoryDao
import com.ali.filmrent.roomDatabase.FilmDao
import com.bumptech.glide.Glide

class BuyFilmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuyFilmBinding
    private lateinit var film: Film
    private lateinit var filmDao: FilmDao
    private lateinit var boughtInventoryDao: BoughtInventoryDao

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyFilmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        filmDao = AppDatabase.getDatabase(this).filmDao
        boughtInventoryDao = AppDatabase.getDatabase(this).boughtInventoryDao
        val film_id: Int = intent.getIntExtra(KEY_FILM_ID, 0)
        val store_id = intent.getIntExtra(KEY_STORE_ID, 0)
        film = filmDao.getFilmById(film_id)


        setSupportActionBar(binding.toolbarFilm)
        binding.collapsingFilm.setExpandedTitleColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        showData()

        binding.decrement.setOnClickListener {
            var num: Int = binding.txtNumberOfFilm.text.toString().toInt()
            if (num > 0) {
                num -= 1
                binding.txtNumberOfFilm.text = num.toString()
            }
        }
        binding.increment.setOnClickListener {
            var num: Int = binding.txtNumberOfFilm.text.toString().toInt()
            if (num < 5) {
                num += 1
                binding.txtNumberOfFilm.text = num.toString()
            }
        }

        binding.btnBuy.setOnClickListener {
            val num: Int = binding.txtNumberOfFilm.text.toString().toInt()
            if (num > 0) {

                val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                dialog.titleText = "Buy Film"
                dialog.confirmText = "Buy"
                dialog.cancelText = "cancel"
                dialog.contentText = "The invoice is \$" + (num * film.amount).toString()
                dialog.setOnCancelListener {
                    dialog.dismiss()
                }
                dialog.setConfirmClickListener {
                    buyFilm(num, store_id, film_id)
                    onBackPressed()
                }
                dialog.show()


            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
        binding.txtFilmTitle.text = film.title
        binding.txtFilmYear.text = film.yearOfRelease.toString()
        binding.txtFilmLength.text = film.length.toString() + " min"
        binding.txtFilmCategory.text = film.category.name
        binding.txtFilmActor.text = film.actor.name
        binding.txtFilmLanguage.text = "Language : " + film.language.name
        binding.txtFilmDescription.text = film.description
        binding.itemRatingbarFilm.rating = film.rating
        binding.txtFilmAmount.text = "Price : " + film.amount.toString() + "$"

        Glide.with(this).load(film.urlImg).into(binding.imgFilm)

        binding.collapsingFilm.title = film.title


    }

    private fun buyFilm(numberOFFilm: Int, store_id: Int, film_id: Int) {
        val invoice = (numberOFFilm * film.amount)
        val manager_id = (AppDatabase.getDatabase(this).storeDao).getStoreManagerId(store_id)
        val wallet =
            (AppDatabase.getDatabase(this).managerDao).getManagerById(manager_id).wallet



        if (wallet >= invoice) {
            (AppDatabase.getDatabase(this).managerDao).updateWallet(manager_id, wallet - invoice)

            for (i in 1..numberOFFilm) {
                boughtInventoryDao.insert(BoughtInventory(film_id = film_id, store_id = store_id))
            }
            Toast.makeText(this, "Your purchase was successful", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Your account balance is insufficient", Toast.LENGTH_SHORT).show()
        }

    }


}