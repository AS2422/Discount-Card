package com.asafin24.discountcard.screens.hand_input

import android.app.Application
import androidx.lifecycle.*
import com.asafin24.discountcard.REPOSITORY
import com.asafin24.discountcard.db.repository.CardsRepository
import com.asafin24.discountcard.model.CardModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HandInputViewModel(application: Application): AndroidViewModel(application) {
    fun add(cardModel: CardModel, onSuccess:() -> Unit) =
        viewModelScope.launch(Dispatchers.IO){
            REPOSITORY.addCard(cardModel){
                onSuccess()
            }
        }
}