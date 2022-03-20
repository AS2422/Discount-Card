package com.asafin24.discountcard.screens.start

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.asafin24.discountcard.APP
import com.asafin24.discountcard.Cards
import com.asafin24.discountcard.MainActivity
import com.asafin24.discountcard.R
import com.asafin24.discountcard.adapter.CardsAdapter
import com.asafin24.discountcard.databinding.FragmentStartBinding
import com.asafin24.discountcard.model.CardModel
import com.asafin24.discountcard.screens.AboutAppFragment
import com.asafin24.discountcard.screens.ExitFragment
import kotlinx.android.synthetic.main.fragment_start.*
import kotlin.system.exitProcess


class StartFragment : Fragment() {

    lateinit var binding: FragmentStartBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CardsAdapter
    private var clicked = false

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim) }
    private val fromButton: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.from_button_add) }
    private val toButton: Animation by lazy { AnimationUtils.loadAnimation(activity, R.anim.to_button_add) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(layoutInflater, container, false)

        //выход из приложения
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragmentExit = ExitFragment()
                fragmentExit.show(((activity as FragmentActivity).supportFragmentManager),"exitDialog")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        //

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        colorsButton()

    }

    private fun init() {
        val viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        viewModel.initDataBase()
        recyclerView = binding.recyclerView
        adapter = CardsAdapter()
        recyclerView.adapter = adapter

        //добавляем все карты в RV
        viewModel.getAllCards().observe(viewLifecycleOwner, {
                list -> adapter.addCard(list)
            if (list.isEmpty()) {
                binding.tvNoCards.visibility = View.VISIBLE
                binding.searchView.visibility = View.INVISIBLE
            } else {
                binding.tvNoCards.visibility = View.GONE
                binding.searchView.visibility = View.VISIBLE
            }
        })
        //

        //поиск карты
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){
                    searchDatabase(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null){
                    searchDatabase(query)
                }
                return true
            }
        })
        //

        binding.buttonHandInput.setOnClickListener {
            onAddButtonClicked()
            binding.tvButtonAdd.visibility = View.INVISIBLE
            APP.navController.navigate(R.id.action_startFragment_to_handInputFragment)
        }

        binding.buttonScan.setOnClickListener {
            onAddButtonClicked()
            binding.tvButtonAdd.visibility = View.INVISIBLE
            APP.navController.navigate(R.id.action_startFragment_to_scannerFragment)
        }

        binding.buttonAdd.setOnClickListener {
            onAddButtonClicked()
        }

        binding.ibSetting.setOnClickListener {
            APP.navController.navigate(R.id.action_startFragment_to_settingFragment)
        }

    }

    //функция поиска карты
    private fun searchDatabase(query: String) {

        val searchQuery = "%$query%"
        val viewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        viewModel.searchDatabase(searchQuery).observe(this, {list ->
            list.let {
                adapter.addCard(it)
            }
        })
    }

    //установка цвета кнопок добавления карты
    private fun colorsButton() {
        buttonAdd.setColorFilter(Color.argb(255, 255, 255, 255))
        buttonScan.setColorFilter(Color.argb(255, 255, 255, 255))
        buttonHandInput.setColorFilter(Color.argb(255, 255, 255, 255))
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked

    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.buttonScan.visibility = View.VISIBLE
            binding.buttonHandInput.visibility = View.VISIBLE
            binding.tvButtonScan.visibility = View.VISIBLE
            binding.tvButtonHandInput.visibility = View.VISIBLE
            binding.tvButtonAdd.visibility = View.INVISIBLE
        } else {
            binding.buttonScan.visibility = View.INVISIBLE
            binding.buttonHandInput.visibility = View.INVISIBLE
            binding.tvButtonScan.visibility = View.INVISIBLE
            binding.tvButtonHandInput.visibility = View.INVISIBLE
            binding.tvButtonAdd.visibility = View.VISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.buttonScan.startAnimation(fromBottom)
            binding.buttonHandInput.startAnimation(fromBottom)
            binding.buttonAdd.startAnimation(rotateOpen)
            binding.tvButtonAdd.startAnimation(toButton)
        } else {
            binding.buttonScan.startAnimation(toBottom)
            binding.buttonHandInput.startAnimation(toBottom)
            binding.buttonAdd.startAnimation(rotateClose)
            binding.tvButtonAdd.startAnimation(fromButton)
        }
    }

    //передача параметров выбранной карты в Bundle для DetailFragment
    companion object{
        fun clickCard(cardModel: CardModel) {
            val bundle = Bundle()
            bundle.putSerializable("card", cardModel)
            APP.navController.navigate(R.id.action_startFragment_to_detailFragment, bundle)

        }

    }


}