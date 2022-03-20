package com.asafin24.discountcard.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asafin24.discountcard.Cards
import com.asafin24.discountcard.R
import com.asafin24.discountcard.databinding.CardItemBinding
import com.asafin24.discountcard.databinding.FragmentSettingBinding
import com.asafin24.discountcard.model.CardModel
import com.asafin24.discountcard.screens.start.StartFragment
import kotlinx.android.synthetic.main.card_item.view.*



class CardsAdapter: RecyclerView.Adapter<CardsAdapter.CardsViewHolder>() {

    var cards = emptyList<CardModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardsViewHolder(view)

    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {

        val category = cards[position].category

        holder.itemView.cardname.text = cards[position].name
        holder.itemView.iconCategory.setImageBitmap(cards[position].iconCategory)
        holder.itemView.iconCategory.setColorFilter(when (category) {
            "Продукты" -> Color.parseColor("#53CC59")
            "Одежда и обувь" -> Color.parseColor("#AE65BA")
            "Красота и здоровье" -> Color.parseColor("#DD5166")
            "Бытовая техника и электроника" -> Color.parseColor("#229CD3")
            "Спорттовары" -> Color.parseColor("#CA8F39")
            "Другое" -> Color.parseColor("#3BB1A6")
            else -> Color.argb(255, 0, 0, 0)
        })
        holder.itemView.setOnClickListener {}

    }

    override fun getItemCount(): Int {
        return cards.size
    }

    class CardsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//        fun bind(model: CardModel) {
//            itemView.car.text = model.symbol
//
//            //проверяем имя криптовалюты на null, и устанавливаем пустую строку если true
//            itemView.coinsItemNameTextView.text = model.name.emptyIfNull()
//
//            //добавляем символ доллара к цене
//            itemView.coinsItemPriceTextView.text = model.price.dollarString()
//
//            //проверям упала ли цена, если да то устанавливае красный цвет и  стрелку вниз
//            //в противном случае устанавливаем зеленый текст и стрелку вверх
//            PriceHelper.showChangePrice(itemView.coinsItemChangeTextView, model.changePercent)
//
//            //устанавливаем иконку для кнопки избранного
//            itemView.favoriteImageView.setImageResource(
//                if (model.isFavourite) R.drawable.item_favorite
//                else R.drawable.item_favorite_border
//            )
//
//            //обрабатываем нажатие на кнопку добавления/удаления избранного
//            itemView.favoriteImageView.setOnClickListener {
//                onItemClickCallback.onFavoriteClick(model.symbol)
//            }
//
//            //загружаем изображение криптовалюты
//            ImageLoader.loadImage(itemView.coinsItemImageView, model.image ?: "")
//
//            //обрабатываем клик по элементу списка
//            itemView.setOnClickListener {
//                onItemClickCallback.onItemClick(
//                    model.symbol,
//                    model.id ?: model.symbol
//                )
//            }
//        }
    }


    fun addCard(list: List<CardModel>) {
        cards = list
        notifyDataSetChanged()
    }


    override fun onViewAttachedToWindow(holder: CardsViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener{
            StartFragment.clickCard(cards[holder.adapterPosition])
        }

    }

    override fun onViewDetachedFromWindow(holder: CardsViewHolder) {
        holder.itemView.setOnClickListener(null)
    }
}