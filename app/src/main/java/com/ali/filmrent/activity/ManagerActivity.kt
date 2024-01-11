package com.ali.filmrent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ali.filmrent.R
import com.ali.filmrent.dataClass.Manager
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityManagerBinding
import com.ali.filmrent.databinding.AddStoreDialogBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.ManagerDao
import com.ali.filmrent.roomDatabase.StoreDao
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class ManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManagerBinding
    private lateinit var managerDao: ManagerDao
    private lateinit var storeDao: StoreDao
    private lateinit var manager: Manager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get manager from database
        storeDao = AppDatabase.getDatabase(this).storeDao
        managerDao = AppDatabase.getDatabase(this).managerDao
        val managerId: Int = intent.getIntExtra(KEY_MANAGER_ID, 0)
        manager = managerDao.returnLoginManager(managerId)



        showStore(managerId)
        binding.toolBarManager.title = "stores"
        setSupportActionBar(binding.toolBarManager)


        binding.fabButtom.setOnClickListener {
            if (storeDao.listOfManagerStore(managerId).size < 2) {
                val dialog = AlertDialog.Builder(this).create()
                val dialogBinding = AddStoreDialogBinding.inflate(layoutInflater)
                dialog.setView(dialogBinding.root)
                dialog.setCancelable(true)
                dialog.show()
                dialogBinding.btnDone.setOnClickListener {

                    if (dialogBinding.edtName.length() > 0 &&
                        dialogBinding.edtPhone.length() > 0
                    ) {
                        val name = dialogBinding.edtName.text.toString()
                        val phone = dialogBinding.edtPhone.text.toString()
                        val listOfUrl = (AppDatabase.getDatabase(this).imgStoreDao).listOfUrl()
                        val url = listOfUrl[storeDao.listOfAllStore().size % listOfUrl.size]
                        val newStore = Store(
                            manager_id = managerId,
                            name = name,
                            phoneNumber = phone,
                            rating = 0.0f,
                            url = url
                        )
                        storeDao.insertStore(newStore)
                        showStore(managerId)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter all of text", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            } else {
                Toast.makeText(this, "You can only have 2 stores", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun showStore(managerId: Int) {
        val listOfStore = storeDao.listOfManagerStore(managerId)

        if (listOfStore.size == 2) {
            Glide
                .with(this)
                .load(listOfStore[0].url)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(32, 8)))
                .into(binding.imgStore1)
            binding.txtNameStore1.text = listOfStore[0].name
            binding.txtPhone1.text = listOfStore[0].phoneNumber
            binding.ratingBar1.rating = listOfStore[0].rating
            binding.card1.visibility = MaterialCardView.VISIBLE

            Glide
                .with(this)
                .load(listOfStore[1].url)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(32, 8)))
                .into(binding.imgStore1)
            binding.txtNameStore2.text = listOfStore[1].name
            binding.txtPhone2.text = listOfStore[1].phoneNumber
            binding.ratingBar2.rating = listOfStore[1].rating
            binding.card2.visibility = MaterialCardView.VISIBLE


        } else if (listOfStore.size == 1) {
            Glide
                .with(this)
                .load(listOfStore[0].url)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(32, 8)))
                .into(binding.imgStore1)
            binding.txtNameStore1.text = listOfStore[0].name
            binding.txtPhone1.text = listOfStore[0].phoneNumber
            binding.ratingBar1.rating = listOfStore[0].rating
            binding.card1.visibility = MaterialCardView.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_exit -> {
                val dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                dialog.titleText = "log out !"
                dialog.confirmText = "confirm"
                dialog.cancelText = "cancel"
                dialog.contentText = "log out?"
                dialog.setOnCancelListener {
                    dialog.dismiss()
                }
                dialog.setConfirmClickListener {
                    dialog.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
                dialog.show()

            }
        }
        return true
    }

}
