package com.ali.filmrent.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.databinding.ActivityLoginBinding
import com.ali.filmrent.databinding.LoginDialogBinding
import com.ali.filmrent.databinding.SignupDialogBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CustomerDao

class CustomerLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var customerDao: CustomerDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView2.text = "you are login as customer"
        customerDao = AppDatabase.getDatabase(this).customerDao

        binding.btnSignup.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val dialogBinding = SignupDialogBinding.inflate(layoutInflater)
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
                    dialogBinding.edtPassword.length() > 0 &&
                    dialogBinding.edtUsername.length() > 0
                ) {
                    val name = dialogBinding.edtName.text.toString()
                    val lastName = dialogBinding.edtLastName.text.toString()
                    val phone = dialogBinding.edtPhoneNumber.text.toString()
                    val email = dialogBinding.edtEmail.text.toString()
                    val user = dialogBinding.edtUsername.text.toString()
                    val pass = dialogBinding.edtPassword.text.toString()



                    if (customerDao.getPassword(user) == "0") {
                        val newCustomer = Customer(
                            firstname = name,
                            lastname = lastName,
                            phoneNumber = phone,
                            username = user,
                            password = pass,
                            wallet = 100,
                            email = email
                        )

                        customerDao.insertCustomer(newCustomer)

                        dialog.dismiss()
                        Toast.makeText(this, " Register successfully", Toast.LENGTH_LONG).show()

                    } else {
                        dialogBinding.textInputUsername.error = "Username already exists"
                    }

                } else {
                    Toast.makeText(this, "Please enter all of text", Toast.LENGTH_LONG).show()
                }

            }

            dialogBinding.btnHaveAccount.setOnClickListener {
                dialog.dismiss()

            }
        }


        binding.btnLogin.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val dialogBinding = LoginDialogBinding.inflate(layoutInflater)
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

                if (
                    dialogBinding.edtPassword.length() > 0 &&
                    dialogBinding.edtUsername.length() > 0
                ) {
                    val user = dialogBinding.edtUsername.text.toString()
                    val pass = dialogBinding.edtPassword.text.toString()


                    if (customerDao.getPassword(user) != "0") {
                        if(customerDao.getPassword(user) == pass) {
                            dialog.dismiss()
                            Toast.makeText(this, "login successfully", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this, "Password is wrong", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        dialogBinding.textInputUsername.error = "Username not exists"
                    }

                } else {
                    Toast.makeText(this, "Please enter all of text", Toast.LENGTH_LONG).show()
                }


            }

            dialogBinding.btnNotHaveAccount.setOnClickListener {
                dialog.dismiss()
            }
        }

    }

}