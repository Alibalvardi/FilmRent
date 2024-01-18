package com.ali.filmrent.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.dataClass.Film
import com.ali.filmrent.databinding.ItemFilmBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class FilmAdapter(
    private val films: ArrayList<Film>,
    val store_id: Int,
    val itemEvents: FilmEvents,
    val database: AppDatabase
) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {

    lateinit var binding: ItemFilmBinding

    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindViews(film: Film) {
            Glide
                .with(itemView.context)
                .load(film.urlImg)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(32, 8)))
                .into(binding.imgItemFilm)

            binding.txtFilmName.text = film.title
            binding.txtFilmYear.text = film.yearOfRelease.toString()
            binding.txtFilmCategory.text = film.category.name
            binding.txtFilmLength.text = film.length.toString() + " min"
            binding.txtFilmActor.text = film.actor.name
            binding.txtFilmLanguage.text = film.language.name
            binding.txtFilmDescription.text = film.description
            binding.itemRatingBarFilm.rating = film.rating
            binding.txtFilmNumber.text = (adapterPosition + 1).toString()

            if (store_id == 0) {
                binding.txtInformation.text = "Price : $10"
            } else {
                binding.txtInformation.text =
                    "Numbers : " + database.boughtInventoryDao.countOfFilm(store_id, film.film_id!!)
            }

            itemView.setOnClickListener {
                itemEvents.onClickedItem(film)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        binding = ItemFilmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bindViews(films[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return films.size
    }
}