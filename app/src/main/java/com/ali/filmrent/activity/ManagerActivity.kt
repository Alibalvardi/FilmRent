package com.ali.filmrent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ali.filmrent.R
import com.ali.filmrent.adapter.StoreAdapter
import com.ali.filmrent.adapter.StoreEvents
import com.ali.filmrent.dataClass.Manager
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.ActivityManagerBinding
import com.ali.filmrent.databinding.AddStoreDialogBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.ManagerDao
import com.ali.filmrent.roomDatabase.StoreDao
const val KEY_STORE_ID = "key_store_id"

class ManagerActivity : AppCompatActivity() , StoreEvents {
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
        manager = managerDao.returnManagerById(managerId)

        showStore()


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

                        showStore()

                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Please enter all of text", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                dialogBinding.btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
            } else {
                Toast.makeText(this, "You can only have 2 stores", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun showStore() {
        val storeList : List<Store> = storeDao.listOfManagerStore(manager.manager_id!!)
        val adapter = StoreAdapter(  ArrayList(storeList), 0,this , AppDatabase.getDatabase(this))
        binding.recycleStoresManager.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recycleStoresManager.adapter = adapter
    }

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
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

    override fun onClickedItem(store: Store) {
        val intent = Intent(this , StoreActivity::class.java)
        intent.putExtra(KEY_STORE_ID,store.store_id)
        startActivity(intent)
    }

    override fun onLongClickedItem(store: Store) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        dialog.titleText = "delete store"
        dialog.confirmText = "Delete"
        dialog.cancelText = "cancel"
        dialog.contentText = "delete "+ store.name
        dialog.setOnCancelListener {
            dialog.dismiss()
        }
        dialog.setConfirmClickListener {
            dialog.dismiss()
            storeDao.deleteStore(store)
            showStore()
        }
        dialog.show()
    }


}
