package com.ali.filmrent.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ali.filmrent.activity.KEY_STORE_ID
import com.ali.filmrent.activity.StoreActivity
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityStoreBinding
import com.ali.filmrent.databinding.AddStoreDialogBinding
import com.ali.filmrent.databinding.FragmentProfileStoreBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.StoreDao
import com.bumptech.glide.Glide

class FragmentProfileStore : Fragment() {

    private lateinit var binding: FragmentProfileStoreBinding
    private lateinit var storeDao: StoreDao
    private lateinit var storeBinding : ActivityStoreBinding
    private lateinit var store: Store
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileStoreBinding.inflate(inflater, container, false)
        storeBinding = ActivityStoreBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val store_id: Int = activity?.intent!!.getIntExtra(KEY_STORE_ID, 0)
        storeDao = AppDatabase.getDatabase(this.requireContext()).storeDao
        store = storeDao.getStoreById(store_id)

        showData()

        binding.fabEditProfile.setOnClickListener {

            val dialog = AlertDialog.Builder(this.requireContext()).create()
            val dialogBinding = AddStoreDialogBinding.inflate(layoutInflater)
            dialogBinding.txtInf.text = "Edit store"
            dialogBinding.btnDone.text = "edit"
            dialogBinding.edtName.setText(store.name)
            dialogBinding.edtPhone.setText(store.phoneNumber)
            dialog.setView(dialogBinding.root)
            dialog.setCancelable(true)
            dialog.show()
            dialogBinding.btnDone.setOnClickListener {

                if (dialogBinding.edtName.length() > 0 &&
                    dialogBinding.edtPhone.length() > 0
                ) {
                    val name = dialogBinding.edtName.text.toString()
                    val phone = dialogBinding.edtPhone.text.toString()
                    val newStore = Store(
                        store_id = store.store_id,
                        manager_id = store.manager_id,
                        name = name,
                        phoneNumber = phone,
                        rating = store.rating,
                        url = store.url
                    )
                    storeDao.updateStore(newStore)
                    store = storeDao.getStoreById(store_id)
                    showData()
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        "Please enter all of text",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
            dialogBinding.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
        }

    }


    @SuppressLint("SetTextI18n")
    private fun showData() {
        binding.collapsingStoreProfile.title = store.name
        Glide.with(this).load(store.url).into(binding.imgStoreProfile)
        binding.txtStoreName.text = "Store name : " + store.name
        binding.txtStorePhone.text = "Phone : " + store.phoneNumber
        binding.txtNumberOfFilm.text =
            "The Number of stors film : " + (AppDatabase.getDatabase(this.requireContext())).boughtInventoryDao.getStoreFilms(
                store.store_id!!
            ).size
        binding.txtNumberOfCopies.text =
            "The number of stores all copies : " + (AppDatabase.getDatabase(this.requireContext())).boughtInventoryDao.getStoreAllCopies(
                store.store_id!!
            ).size
        binding.itemRatingBarStore.rating = store.rating


    }
}