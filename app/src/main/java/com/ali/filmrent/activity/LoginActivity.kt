package com.ali.filmrent.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Manager
import com.ali.filmrent.databinding.ActivityLoginBinding
import com.ali.filmrent.databinding.LoginDialogBinding
import com.ali.filmrent.databinding.SignupDialogBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.ManagerDao

const val KEY_MANAGER_ID = "manager_id"
const val KEY_CUSTOMER_ID = "customer_id"

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var customerDao: CustomerDao
    private lateinit var managerDao: ManagerDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView2.text = "Login Page"
        customerDao = AppDatabase.getDatabase(this).customerDao
        managerDao = AppDatabase.getDatabase(this).managerDao

        binding.btnSignup.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val dialogBinding = SignupDialogBinding.inflate(layoutInflater)
            dialog.setView(dialogBinding.root)
            dialog.setCancelable(true)
            //dialogBinding.radioBtnManager.isChecked = true
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
                    dialogBinding.edtUsername.length() > 0 &&
                    (dialogBinding.radioBtnManager.isChecked || dialogBinding.radioBtnCustomer.isChecked)
                ) {
                    val name = dialogBinding.edtName.text.toString()
                    val lastName = dialogBinding.edtLastName.text.toString()
                    val phone = dialogBinding.edtPhoneNumber.text.toString()
                    val email = dialogBinding.edtEmail.text.toString()
                    val user = dialogBinding.edtUsername.text.toString()
                    val pass = dialogBinding.edtPassword.text.toString()
                    val isManager: Boolean = dialogBinding.radioBtnManager.isChecked

                    if (isManager) {
                        if (customerDao.getPassword(user) == "0" && managerDao.getPassword(user) == "0") {
                            val newManager = Manager(
                                firstname = name,
                                lastname = lastName,
                                phoneNumber = phone,
                                username = user,
                                password = pass,
                                wallet = 100,
                                email = email
                            )

                            managerDao.insertManager(newManager)

                            dialog.dismiss()
                            Toast.makeText(
                                this,
                                " Register as manager successfully",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            dialogBinding.textInputUsername.error = "Username already exists"
                        }
                    } else {
                        if (customerDao.getPassword(user) == "0" && managerDao.getPassword(user) == "0") {
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
                            Toast.makeText(
                                this,
                                " Register ad customer successfully",
                                Toast.LENGTH_LONG
                            ).show()

                        } else {
                            dialogBinding.textInputUsername.error = "Username already exists"
                        }
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

                    if (customerDao.getPassword(user) != "0" || managerDao.getPassword(user) != "0") {

                        val isManager: Boolean = managerDao.getPassword(user) != "0"
                        if (isManager) {
                            if (managerDao.getPassword(user) == pass) {
                                dialog.dismiss()

                                val intent = Intent(this, ManagerActivity::class.java)
                                intent.putExtra(KEY_MANAGER_ID, managerDao.getId(user))
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                Toast.makeText(
                                    this,
                                    "login as manager successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(this, "Password is wrong", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            if (customerDao.getPassword(user) == pass) {
                                dialog.dismiss()
                                val intent = Intent(this, CustomerActivity::class.java)
                                intent.putExtra(KEY_CUSTOMER_ID, customerDao.getId(user))
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                Toast.makeText(
                                    this,
                                    "login as customer successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(this, "Password is wrong", Toast.LENGTH_LONG).show()
                            }
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