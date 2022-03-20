package com.asafin24.discountcard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.GridLayoutAnimationController
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.asafin24.discountcard.adapter.CardsAdapter
import com.asafin24.discountcard.adapter.Communicator
import com.asafin24.discountcard.databinding.ActivityMainBinding
import com.asafin24.discountcard.model.CardModel
import com.asafin24.discountcard.screens.DeleteDialogFragment
import com.asafin24.discountcard.screens.hand_input.HandInputFragment
import com.asafin24.discountcard.screens.scanner.ScannerFragment
import java.io.Serializable


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        APP = this
        navController = Navigation.findNavController(this, R.id.nav_fragment)


        //получение из SharedPreferences состояния "Тёмной темы"
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", Context.MODE_PRIVATE)
        val isNightModeOn = appSettingPrefs.getBoolean("NightMode", false)

        if(isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        //
    }

    //спрятать клавиатуру если фокус на пустом месте
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

}