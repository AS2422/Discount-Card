package com.asafin24.discountcard.adapter

import com.asafin24.discountcard.model.CardModel

interface Communicator {
    fun passDataCommunicator(currentCard: CardModel)
}