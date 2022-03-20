package com.asafin24.discountcard.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.asafin24.discountcard.Cards
import com.asafin24.discountcard.model.CardModel
import java.util.concurrent.Flow

@Dao
interface CardsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(cardModel: CardModel)

    @Delete
    suspend fun delete(cardModel: CardModel)

    @Update
    suspend fun edit(cardModel: CardModel)

    @Update
    suspend fun counter(cardModel: CardModel)

    @Query("SELECT * FROM cards_table ORDER BY counter DESC")
    fun getAllCards(): LiveData<List<CardModel>>

    @Query("SELECT * FROM cards_table WHERE name LIKE :searchQuery")
    fun searchDataBase(searchQuery: String): kotlinx.coroutines.flow.Flow<List<CardModel>>

}