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
import com.asafin24.discountcard.model.CardModel
import com.asafin24.discountcard.screens.detail_card.DetailFragment
import com.asafin24.discountcard.screens.detail_card.DetailViewModel
import kotlinx.android.synthetic.main.fragment_delete_dialog.*
import java.io.Serializable

class DeleteDialogFragment: DialogFragment() {

    lateinit var binding: FragmentDeleteDialogBinding
    lateinit var currentCard: CardModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteDialogBinding.inflate(layoutInflater, container,false)
        currentCard = arguments?.getSerializable("card") as CardModel
        val viewModel = ViewModelProvider(requireActivity()).get(DetailViewModel::class.java)
        binding.tvDeleteDialog.text = "Вы точно хотите удалить карту \"" + currentCard.name + "\"?"

        binding.buttonYes.setOnClickListener {
            viewModel.delete(currentCard){}
            dismiss()
            APP.navController.navigate(R.id.action_detailFragment_to_startFragment)
        }

        binding.buttonNo.setOnClickListener {
            dismiss()
        }

        return binding.root


    }
}