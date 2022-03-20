package com.asafin24.discountcard.screens.detail_card

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.asafin24.discountcard.APP
import com.asafin24.discountcard.R
import com.asafin24.discountcard.adapter.Communicator
import com.asafin24.discountcard.databinding.FragmentDetailBinding
import com.asafin24.discountcard.model.CardModel
import com.asafin24.discountcard.screens.DeleteDialogFragment
import com.asafin24.discountcard.screens.scanner.ScannerViewModel
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment(), Communicator {

    lateinit var binding: FragmentDetailBinding
    lateinit var currentCard: CardModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater, container,false)
        val viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        currentCard = arguments?.getSerializable("card") as CardModel

        binding.btnDeleteCard.setOnClickListener {
            passDataCommunicator(currentCard)
        }

        //изменить карту
        binding.ibEdit.setOnClickListener {
            binding.tvCardNameDetail.isFocusable = true
            binding.tvCardNameDetail.isCursorVisible = true
            binding.tvCardNameDetail.isLongClickable = true
            binding.tvCardNameDetail.isFocusableInTouchMode = true
            binding.tvCardNameDetail.requestFocus()

            //появление клавиатуры
            val key = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            key.showSoftInput(tv_card_name_detail, 0)


            //появление кнопки сохранить
            binding.tvSave.visibility = View.VISIBLE
        }

        //сохранение имени карты
        binding.tvSave.setOnClickListener {
            currentCard.name = binding.tvCardNameDetail.text.toString()
            viewModel.edit(currentCard){}

            Toast.makeText(context, "Имя карты сохранено", Toast.LENGTH_SHORT).show()

            val key = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            key.hideSoftInputFromWindow(tv_card_name_detail.windowToken, 0)

            binding.tvSave.visibility = View.GONE
            binding.tvCardNameDetail.isFocusable = false
            binding.tvCardNameDetail.isCursorVisible = false
            binding.tvCardNameDetail.isLongClickable = false
            binding.tvCardNameDetail.isFocusableInTouchMode = false
        }
        //

        //системная кнопка "назад"
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                currentCard.counter++
                viewModel.updateCount(currentCard){}
                APP.navController.navigate(R.id.action_detailFragment_to_startFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        //

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        //установка параметров карты
        binding.tvCardNameDetail.setText(currentCard.name)
        binding.ivCardCodeDetail.setImageBitmap(currentCard.code)
        binding.tvCardCategory.text = currentCard.category
        binding.iconCategory.setImageBitmap(currentCard.iconCategory)
        binding.numberCard.text = currentCard.numberCard

        //установка фона для иконки
        val iconGradient = (binding.iconCategoryShape.background as GradientDrawable).mutate()
        when(currentCard.category) {
            "Продукты" -> (iconGradient as GradientDrawable).colors = intArrayOf(Color.parseColor("#53CC59"), Color.parseColor("#89D58C"))
            "Одежда и обувь" -> (iconGradient as GradientDrawable).colors = intArrayOf(Color.parseColor("#AE65BA"), Color.parseColor("#B380BC"))
            "Красота и здоровье" -> (iconGradient as GradientDrawable).colors = intArrayOf(Color.parseColor("#DD5166"), Color.parseColor("#DA7584"))
            "Бытовая техника и электроника" -> (iconGradient as GradientDrawable).colors = intArrayOf(Color.parseColor("#229CD3"), Color.parseColor("#61ADCF"))
            "Спорттовары" -> (iconGradient as GradientDrawable).colors = intArrayOf(Color.parseColor("#CA8F39"), Color.parseColor("#CAA064"))
            "Другое" -> (iconGradient as GradientDrawable).colors = intArrayOf(Color.parseColor("#3BB1A6"), Color.parseColor("#6BBAB3"))
        }


        binding.buttonBack.setOnClickListener {
            currentCard.counter++
            viewModel.updateCount(currentCard){}
            APP.navController.navigate(R.id.action_detailFragment_to_startFragment)

        }


    }

    //передаём параметры карты в диалог удаления
    override fun passDataCommunicator(currentCard: CardModel) {
        val bundle = Bundle()
        bundle.putSerializable("card", currentCard)

        val fragmentDeleteDialog = DeleteDialogFragment()
        fragmentDeleteDialog.arguments = bundle
        fragmentDeleteDialog.show(((activity as FragmentActivity).supportFragmentManager),"deleteDialog")
    }

}