package com.ali.filmrent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ItemStoreBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class StoreAdapter(
    private val stores: ArrayList<Store>,
    val filmId :Int ,
    val itemEvents: StoreEvents,
    val appDatabase: AppDatabase
) :
    RecyclerView.Adapter<StoreAdapter.FilmViewHolder>() {

    lateinit var binding: ItemStoreBinding

    inner class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindViews(store: Store) {
            val manager = appDatabase.managerDao.returnManagerById(store.manager_id)
            Glide
                .with(itemView.context)
                .load(store.url)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(32, 8)))
                .into(binding.imgStore)

            binding.txtNameStore.text = store.name
            binding.txtPhone.text = "Phone : " + store.phoneNumber
            binding.txtManagerName.text = "Manager : " + manager.firstname + " " + manager.lastname
            binding.txtManagerPhone.text = "Manager phone : " + manager.phoneNumber
            binding.txtNumberOfFilms.text =
                "the number of films : " + appDatabase.boughtInventoryDao.getStoreFilms(store.store_id!!).size.toString()

            if (filmId==0) {
                binding.txtAvailableCopies.text =
                    "The number of available film copies : " + (appDatabase.boughtInventoryDao.getStoreAllCopies(
                        store.store_id,
                    ).size - appDatabase.rentalDao.countOfActiveRentsOfStore(store.store_id)).toString()
            }else{
                binding.txtAvailableCopies.text =
                    "The number of available film copies : " + (appDatabase.boughtInventoryDao.getStoreFilmCopies(
                        store.store_id,
                        filmId
                    ).size - appDatabase.rentalDao.countOfActiveRentsOfFilmInStore(store.store_id , filmId)).toString()
            }
            binding.ratingBar1.rating = store.rating
            binding.txtStoreNumber.text = (adapterPosition + 1).toString()



            itemView.setOnClickListener {
                itemEvents.onClickedItem(store)
            }
            itemView.setOnLongClickListener {
                itemEvents.onLongClickedItem(store)
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        binding = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bindViews(stores[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return stores.size
    }
}