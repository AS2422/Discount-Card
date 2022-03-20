package com.asafin24.discountcard.screens.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asafin24.discountcard.REPOSITORY
import com.asafin24.discountcard.model.CardModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScannerViewModel: ViewModel() {
    fun add(cardModel: CardModel, onSuccess:() -> Unit) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.addCard(cardModel){
                onSuccess()
            }
        }
}