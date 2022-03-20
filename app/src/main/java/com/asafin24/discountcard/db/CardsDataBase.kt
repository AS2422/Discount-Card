package com.asafin24.discountcard.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.asafin24.discountcard.db.dao.CardsDao
import com.asafin24.discountcard.model.CardModel
import java.security.AccessControlContext

@Database(entities = [CardModel::class], version = 1)
@TypeConverters(Converter::class)
abstract class CardsDataBase: RoomDatabase() {
    abstract fun getCardDao(): CardsDao

    companion object{
        private var database: CardsDataBase ?= null

        @Synchronized
        fun getAdd(context: Context): CardsDataBase{
            return if (database == null){
                database = Room.databaseBuilder(context, CardsDataBase::class.java, "db_cards").build()
                database as CardsDataBase
            } else {
                database as CardsDataBase
            }
        }
    }
}