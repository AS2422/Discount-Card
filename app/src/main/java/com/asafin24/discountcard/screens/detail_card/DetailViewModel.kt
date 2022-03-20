package com.asafin24.discountcard.screens.detail_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asafin24.discountcard.REPOSITORY
import com.asafin24.discountcard.model.CardModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel: ViewModel() {
    fun delete(cardModel: CardModel, onSuccess:() -> Unit) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.deleteCard(cardModel){
                onSuccess()
            }
        }

    fun updateCount(cardModel: CardModel, onSuccess:() -> Unit) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.counterCard(cardModel){
                onSuccess()
            }
        }

    fun edit(cardModel: CardModel, onSuccess:() -> Unit) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.editCard(cardModel){
                onSuccess()
            }
        }

}