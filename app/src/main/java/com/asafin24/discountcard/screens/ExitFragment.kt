package com.asafin24.discountcard.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.asafin24.discountcard.APP
import com.asafin24.discountcard.R
import com.asafin24.discountcard.databinding.FragmentDeleteDialogBinding
import com.asafin24.discountcard.databinding.FragmentExitBinding
import com.asafin24.discountcard.model.CardModel
import com.asafin24.discountcard.screens.detail_card.DetailFragment
import com.asafin24.discountcard.screens.detail_card.DetailViewModel
import kotlinx.android.synthetic.main.fragment_delete_dialog.*
import java.io.Serializable
import kotlin.system.exitProcess

class ExitFragment: DialogFragment() {

    lateinit var binding: FragmentExitBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExitBinding.inflate(layoutInflater, container,false)


        binding.buttonYes.setOnClickListener {
            exitProcess(-1)
        }

        binding.buttonNo.setOnClickListener {
            dismiss()
        }

        return binding.root

    }
}