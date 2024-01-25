package com.ali.filmrent.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.ali.filmrent.activity.KEY_CUSTOMER_ID
import com.ali.filmrent.activity.KEY_STORE_ID
import com.ali.filmrent.dataClass.Customer
import com.ali.filmrent.dataClass.Store
import com.ali.filmrent.databinding.AddStoreDialogBinding
import com.ali.filmrent.databinding.EditUserDialogBinding
import com.ali.filmrent.databinding.FragmentProfileCustomerBinding
import com.ali.filmrent.databinding.FragmentProfileStoreBinding
import com.ali.filmrent.databinding.SignupDialogBinding
import com.ali.filmrent.roomDatabase.AppDatabase
import com.ali.filmrent.roomDatabase.CustomerDao
import com.ali.filmrent.roomDatabase.ManagerDao
import com.ali.filmrent.roomDatabase.RentalDao
import com.ali.filmrent.roomDatabase.ReserveDao
import com.ali.filmrent.roomDatabase.StoreDao

class FragmentProfileCustomer : Fragment() {

    private lateinit var binding: FragmentProfileCustomerBinding
    private lateinit var customerDao: CustomerDao
    private lateinit var managerDao: ManagerDao
    private lateinit var rentalDao: RentalDao
    private lateinit var reserveDao: ReserveDao
    private lateinit var customer: Customer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        customerDao = AppDatabase.getDatabase(this.requireContext()).customerDao
        managerDao = AppDatabase.getDatabase(this.requireContext()).managerDao
        rentalDao = AppDatabase.getDatabase(this.requireContext()).rentalDao
        reserveDao = AppDatabase.getDatabase(this.requireContext()).reserveDao
        val customer_id: Int = activity?.intent!!.getIntExtra(KEY_CUSTOMER_ID, 0)
        customer = customerDao.getCustomerById(customer_id)

        showData()

        binding.fabEditProfile.setOnClickListener {
            val dialog = AlertDialog.Builder(this.requireContext()).create()
            val dialogBinding = EditUserDialogBinding.inflate(layoutInflater)
            dialogBinding.txtInf.text = "Edit Information"
            dialogBinding.btnDone.text = "edit"
            dialogBinding.btnHaveAccount.text = "cancel"
            dialogBinding.edtName.setText(customer.firstname)
            dialogBinding.edtLastName.setText(customer.lastname)
            dialogBinding.edtPhoneNumber.setText(customer.phoneNumber)
            dialogBinding.edtEmail.setText(customer.email)
            dialogBinding.edtUsername.setText(customer.username)
            dialogBinding.edtPassword.setText(customer.password)
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
                    if ((customerDao.getPassword(user) == "0" && managerDao.getPassword(user) == "0") || customer.username == user) {
                        val newCustomer = Customer(
                            customer_id = customer.customer_id!!,
                            firstname = name,
                            lastname = lastName,
                            phoneNumber = phone,
                            username = user,
                            password = pass,
                            wallet = customer.wallet,
                            email = email
                        )
                        customerDao.updateCustomer(newCustomer)
                        customer = customerDao.getCustomerById(customer_id)
                        showData()
                        dialog.dismiss()

                        Toast.makeText(
                            context,
                            "Information edited successfully",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        dialogBinding.textInputUsername.error = "Username already exists"
                    }
                } else {
                    Toast.makeText(
                        this.requireContext(),
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
        binding.txtName.text = "Name : " + customer.firstname + " " + customer.lastname
        binding.txtPhone.text = "Phone : " + customer.phoneNumber
        binding.txtEmail.text = "Email : " + customer.email
        binding.txtUsername.text = "Username : " + customer.username
        binding.txtWallet.text = "Wallet balance : $" + customer.wallet
        binding.txtNumberOfRent.text =
            "The number of all rent : " + (AppDatabase.getDatabase(this.requireContext())).rentalDao.countOfRentsOfCustomer(
                customer.customer_id!!
            )
        binding.txtNumberActiveRents.text =
            "The number of active : " + (AppDatabase.getDatabase(this.requireContext())).rentalDao.countOfActiveRentsOfCustomer(
                customer.customer_id!!
            )
    }

}