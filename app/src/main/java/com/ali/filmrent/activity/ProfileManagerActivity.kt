package com.ali.filmrent.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.ali.filmrent.R
import com.ali.filmrent.dataClass.Manager
import com.ali.filmrent.databinding.ActivityProfileManagerBinding
import com.ali.filmrent.databinding.EditUserDialogBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.ManagerDao

class ProfileManagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileManagerBinding
    private lateinit var customerDao: CustomerDao
    private lateinit var managerDao: ManagerDao
    private lateinit var manager: Manager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customerDao = AppDatabase.getDatabase(this).customerDao
        managerDao = AppDatabase.getDatabase(this).managerDao
        manager = managerDao.getManagerById(intent.getIntExtra(KEY_MANAGER_ID, 0))

        showData()

        binding.fabEditProfile.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val dialogBinding = EditUserDialogBinding.inflate(layoutInflater)
            dialogBinding.txtInf.text = "Edit Information"
            dialogBinding.btnDone.text = "edit"
            dialogBinding.btnHaveAccount.text = "cancel"
            dialogBinding.edtName.setText(manager.firstname)
            dialogBinding.edtLastName.setText(manager.lastname)
            dialogBinding.edtPhoneNumber.setText(manager.phoneNumber)
            dialogBinding.edtEmail.setText(manager.email)
            dialogBinding.edtUsername.setText(manager.username)
            dialogBinding.edtPassword.setText(manager.password)
            dialog.setView(dialogBinding.root)
            dialog.setCancelable(true)
            dialog.show()
            dialogBinding.textInputPassword.editText?.addTextChangedListener {
                if (it!!.length < 4) {
                    dialogBinding.textInputPassword.error = "Password must be at least 4 characters"
                } else {
                    dialogBinding.textInputPassword.error = null
                }
            }
            dialogBinding.textInputUsername.editText?.addTextChangedListener {
                dialogBinding.textInputUsername.error = null
            }
            dialogBinding.btnDone.setOnClickListener {

                if (dialogBinding.edtName.length() > 0 &&
                    dialogBinding.edtLastName.length() > 0 &&
                    dialogBinding.edtPhoneNumber.length() > 0 &&
                    dialogBinding.edtEmail.length() > 0 &&
                    dialogBinding.edtPassword.length() > 3 &&
                    dialogBinding.edtUsername.length() > 0
                ) {
                    val name = dialogBinding.edtName.text.toString()
                    val lastName = dialogBinding.edtLastName.text.toString()
                    val phone = dialogBinding.edtPhoneNumber.text.toString()
                    val email = dialogBinding.edtEmail.text.toString()
                    val user = dialogBinding.edtUsername.text.toString()
                    val pass = dialogBinding.edtPassword.text.toString()
                    if ((customerDao.getPassword(user) == "0" && managerDao.getPassword(user) == "0") || manager.username == user) {
                        val newManager = Manager(
                            manager_id = manager.manager_id!!,
                            firstname = name,
                            lastname = lastName,
                            phoneNumber = phone,
                            username = user,
                            password = pass,
                            wallet = manager.wallet,
                            email = email
                        )
                        managerDao.updateCustomer(newManager)
                        manager = managerDao.getManagerById(manager.manager_id!!)
                        showData()
                        dialog.dismiss()

                        Toast.makeText(
                            this,
                            "Information edited successfully",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        dialogBinding.textInputUsername.error = "Username already exists"
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Please enter all of text",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
            dialogBinding.btnHaveAccount.setOnClickListener {
                dialog.dismiss()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showData() {
        binding.txtName.text = "Name : " + manager.firstname + " " + manager.lastname
        binding.txtPhone.text = "Phone : " + manager.phoneNumber
        binding.txtEmail.text = "Email : " + manager.email
        binding.txtUsername.text = "Username : " + manager.username
        binding.txtWallet.text = "Wallet balance : $" + manager.wallet
    }
}