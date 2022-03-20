package com.asafin24.discountcard.screens.hand_input

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.asafin24.discountcard.APP
import com.asafin24.discountcard.R
import com.asafin24.discountcard.databinding.FragmentHandInputBinding
import com.asafin24.discountcard.model.CardModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.oned.EAN13Writer
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_hand_input.*
import java.lang.Exception


class HandInputFragment: Fragment() {

    lateinit var binding: FragmentHandInputBinding
    private var category: String = ""
    lateinit var iconCategory: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHandInputBinding.inflate(layoutInflater, container, false)

        //системная кнопка "назад"
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                APP.navController.navigate(R.id.action_handInputFragment_to_startFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        //

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.root.setOnClickListener {
            noFocus()
        }

        binding.edittextCardName.setOnClickListener {
            focusCardName()
        }

        binding.edittextInputCard.setOnClickListener {
            focusInputCard()
        }

        binding.edittextCardName.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                focusInputCard()
                return@OnKeyListener true
            }
            false
        })

        binding.edittextInputCard.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                noFocus()
                return@OnKeyListener true
            }
            false
        })


        val viewModel = ViewModelProvider(this).get(HandInputViewModel::class.java)

        popupMenu(fragment_hand_input)

        binding.addBtn.setOnClickListener {


            val data = binding.edittextInputCard.text.toString().trim()
            if (data.isEmpty()) {
                binding.tvError.visibility = View.VISIBLE
            } else {
                val writter = EAN13Writer()
                try {
                    val bitMatrix = writter.encode(data, BarcodeFormat.EAN_13, 1024, 402)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    for (x in 0 until width) {
                        for (y in 0 until height) {
                            bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                        }
                    }
                    binding.ivCode.setImageBitmap(bmp)
                    val name = binding.edittextCardName.text.toString()
                    val code = binding.ivCode.drawable.toBitmap()
                    val numberCard = binding.edittextInputCard.text.toString()
                    binding.numberCard.text = numberCard

                    //проверка на пустоту имени и отсутствие категории
                    if (name.isNotEmpty() && category.isNotEmpty()) {

                        binding.tvError.visibility = View.GONE

                        viewModel.add(CardModel(name = name, code = code, category = category, iconCategory = iconCategory, numberCard = numberCard)) {}
                        Toast.makeText(requireActivity(),"Карта успешно добавлена", Toast.LENGTH_SHORT).show()
                        APP.navController.navigate(R.id.action_handInputFragment_to_startFragment)
                    } else {
                        binding.tvError.text = "Необходимо ввести название карты и указать категорию"
                        binding.tvError.visibility = View.VISIBLE
                    }
                    //

                } catch (e: WriterException) {
                    e.printStackTrace()
                } catch (e: IllegalArgumentException) {
                    binding.tvError.text = "Некорректный код. Необходимо ввести 13 цифр под штрихкодом"
                    binding.tvError.visibility = View.VISIBLE
                }
            }

        }

        binding.buttonBackHif.setOnClickListener {
            APP.navController.navigate(R.id.action_handInputFragment_to_startFragment)
        }
    }

    //выползающее меню категорий
    @SuppressLint("DiscouragedPrivateApi")
    private fun popupMenu(view: View) {
        val popupMenu = androidx.appcompat.widget.PopupMenu(view.context,tv_category)
        popupMenu.inflate(R.menu.categories_menu)
        popupMenu.setOnMenuItemClickListener {
            category = it.toString()
            iconCategory = it.icon.toBitmap()

            tv_category.text = it.toString()
            true
        }

        binding.tvCategory.setOnClickListener {

            noFocus()

            try {
                val popup = androidx.appcompat.widget.PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true

                val menu = popup.get(popupMenu)
                menu.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu,true)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                popupMenu.show()
            }
        }
    }

    private fun focusInputCard() {
        binding.edittextInputCard.isFocusable = true
        binding.edittextInputCard.isFocusableInTouchMode = true
        binding.edittextInputCard.requestFocus()
        val key = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        key.showSoftInput(edittext_input_card, 0)
    }

    private fun focusCardName() {
        binding.edittextCardName.isFocusable = true
        binding.edittextCardName.isFocusableInTouchMode = true
        binding.edittextCardName.requestFocus()
        val key = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        key.showSoftInput(edittext_card_name, 0)
    }

    private fun noFocus() {
        binding.edittextCardName.isFocusable = false
        binding.edittextInputCard.isFocusable = false
    }
}