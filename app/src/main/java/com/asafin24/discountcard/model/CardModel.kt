package com.asafin24.discountcard.model


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import android.widget.ImageView
import java.io.Serializable

@Entity (tableName = "cards_table")
data class CardModel (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo
    var name: String,

    @ColumnInfo
    var code: Bitmap,

    @ColumnInfo
    var category: String,

    @ColumnInfo
    var iconCategory: Bitmap,

    @ColumnInfo
    var numberCard: String,

    @ColumnInfo
    var counter: Int = 0
    ) : Serializable
