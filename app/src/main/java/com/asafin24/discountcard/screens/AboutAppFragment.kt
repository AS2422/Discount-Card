package com.asafin24.discountcard.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.asafin24.discountcard.databinding.FragmentAboutAppBinding
import com.asafin24.discountcard.databinding.FragmentDeleteDialogBinding


class AboutAppFragment : DialogFragment() {
    lateinit var binding: FragmentAboutAppBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentAboutAppBinding.inflate(layoutInflater, container,false)

        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        return binding.root
    }


}