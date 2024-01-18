package com.ali.filmrent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ali.filmrent.databinding.FragmentMyfilmCustomerBinding
import com.ali.filmrent.databinding.FragmentStoresCustomerBinding

class FragmentMyStoresCustomer : Fragment() {

    private lateinit var binding: FragmentStoresCustomerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoresCustomerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}