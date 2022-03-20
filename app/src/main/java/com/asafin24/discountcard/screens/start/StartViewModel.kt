package com.asafin24.discountcard.screens.start

import android.app.Application
import androidx.lifecycle.*
import com.asafin24.discountcard.Cards
import com.asafin24.discountcard.REPOSITORY
import com.asafin24.discountcard.db.CardsDataBase
import com.asafin24.discountcard.db.repository.CardsRealization
import com.asafin24.discountcard.db.repository.CardsRepository
import com.asafin24.discountcard.model.CardModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartViewModel(application: Application): AndroidViewModel(application) {

    val context = application
    fun initDataBase(){
        val daoCard = CardsDataBase.getAdd(context).getCardDao()
        REPOSITORY = CardsRealization(daoCard)
    }

    fun getAllCards(): LiveData<List<CardModel>>{
        return REPOSITORY.allCards
    }

    fun searchDatabase(searchQuery: String): LiveData<List<CardModel>> {
        return REPOSITORY.searchDataBase(searchQuery).asLiveData()
    }

}