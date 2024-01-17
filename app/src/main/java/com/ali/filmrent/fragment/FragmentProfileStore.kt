package com.ali.filmrent.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ali.filmrent.databinding.ActivityStoreBinding
import com.ali.filmrent.databinding.FragmentMyFilmStoreBinding
import com.ali.filmrent.databinding.FragmentProfileStoreBinding

class FragmentProfileStore : Fragment() {

    private lateinit var binding: FragmentProfileStoreBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileStoreBinding.inflate(inflater ,container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}