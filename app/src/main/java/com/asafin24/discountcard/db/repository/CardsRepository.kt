package com.asafin24.discountcard.db.repository

import androidx.lifecycle.LiveData
import com.asafin24.discountcard.Cards
import com.asafin24.discountcard.model.CardModel
import kotlinx.coroutines.flow.Flow

interface CardsRepository {
    val allCards: LiveData<List<CardModel>>
    suspend fun addCard(cardModel: CardModel, onSuccess:() -> Unit)
    suspend fun deleteCard(cardModel: CardModel, onSuccess:() -> Unit)
    suspend fun editCard(cardModel: CardModel, onSuccess: () -> Unit)
    suspend fun counterCard(cardModel: CardModel, onSuccess: () -> Unit)
    fun searchDataBase(searchQuery: String): Flow<List<CardModel>>
}