package com.asafin24.discountcard.db.repository

import androidx.lifecycle.LiveData
import com.asafin24.discountcard.Cards
import com.asafin24.discountcard.db.dao.CardsDao
import com.asafin24.discountcard.model.CardModel
import kotlinx.coroutines.flow.Flow

class CardsRealization(private val cardsDao: CardsDao): CardsRepository {
    override val allCards: LiveData<List<CardModel>>
        get() = cardsDao.getAllCards()


    override suspend fun addCard(cardModel: CardModel, onSuccess: () -> Unit) {
        cardsDao.add(cardModel)
        onSuccess()
    }

    override suspend fun deleteCard(cardModel: CardModel, onSuccess: () -> Unit) {
        cardsDao.delete(cardModel)
        onSuccess()
    }

    override suspend fun editCard(cardModel: CardModel, onSuccess: () -> Unit) {
        cardsDao.edit(cardModel)
        onSuccess()
    }

    override suspend fun counterCard(cardModel: CardModel, onSuccess: () -> Unit) {
        cardsDao.counter(cardModel)
        onSuccess()
    }

    override fun searchDataBase(searchQuery: String): Flow<List<CardModel>> {
        return cardsDao.searchDataBase(searchQuery)
    }

}