package com.asafin24.discountcard.screens.scanner

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.asafin24.discountcard.APP
import com.asafin24.discountcard.CAMERA_REQUEST_CODE
import com.asafin24.discountcard.R
import com.asafin24.discountcard.databinding.FragmentScannerBinding
import com.asafin24.discountcard.model.CardModel
import com.asafin24.discountcard.screens.hand_input.HandInputViewModel
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.oned.EAN13Writer
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.fragment_hand_input.*
import kotlinx.android.synthetic.main.fragment_scanner.*
import kotlinx.android.synthetic.main.fragment_scanner.tv_category
import java.lang.Exception
import java.lang.NullPointerException


class ScannerFragment : Fragment() {

    lateinit var binding: FragmentScannerBinding
    private lateinit var codeScanner: CodeScanner
    private var category: String = ""
    lateinit var iconCategory: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //разрешение на камеру
        setupPermissions()
        //
        binding = FragmentScannerBinding.inflate(layoutInflater, container,false)

        //системная кнопка "назад"
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                APP.navController.navigate(R.id.action_scannerFragment_to_startFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
        //

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.root.setOnClickListener {
            binding.edittextCardNameScanner.isFocusable = false
        }

        binding.edittextCardNameScanner.setOnClickListener {
            binding.edittextCardNameScanner.isFocusable = true
            binding.edittextCardNameScanner.isFocusableInTouchMode = true
            binding.edittextCardNameScanner.requestFocus()
            val key = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            key.showSoftInput(edittext_card_name_scanner, 0)
        }

        binding.edittextCardNameScanner.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.edittextCardNameScanner.isFocusable = false
                return@OnKeyListener true
            }
            false
        })

        val viewModel = ViewModelProvider(this).get(ScannerViewModel::class.java)
        val scannerView = binding.scannerView
        val activity = requireActivity()
        popupMenu(fragment_scanner)

        //настройка сканнера
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false
        }


        //расшировка сканнера и добавление карты
        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                val data = it.text?.toString()?.trim()
                    try {
                        val writter_ean13 = EAN13Writer()
                        val bitMatrix = writter_ean13.encode(data, BarcodeFormat.EAN_13, 1024, 402)
                        val width = bitMatrix.width
                        val height = bitMatrix.height
                        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                        for (x in 0 until width) {
                            for (y in 0 until height) {
                                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                            }
                        }
                        binding.ivScanner.setImageBitmap(bmp)
                        binding.numberCard.text = it.toString()
                    } catch (e: IllegalArgumentException) {
                        val writter_qr = QRCodeWriter()
                        val bitMatrix = writter_qr.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                        val width = bitMatrix.width
                        val height = bitMatrix.height
                        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                        for (x in 0 until width) {
                            for (y in 0 until height) {
                                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                            }
                        }
                        binding.ivScanner.setImageBitmap(bmp)
                        binding.numberCard.text = it.toString()
                }
            }
        }

        binding.addBtnScanner.setOnClickListener {
            val name: String = binding.edittextCardNameScanner.text.toString()
            val code: Bitmap
            if (name.isNotEmpty() && category.isNotEmpty()) {
                tv_scanner_empty.visibility = View.GONE
                try {
                    code = binding.ivScanner.drawable.toBitmap()
                    val numberCard = binding.numberCard.text.toString()
                    viewModel.add(CardModel(name = name, code = code, category = category, iconCategory = iconCategory, numberCard = numberCard)) {}
                    Toast.makeText(requireActivity(),"Карта успешно добавлена", Toast.LENGTH_SHORT).show()
                    APP.navController.navigate(R.id.action_scannerFragment_to_startFragment)
                } catch (e: NullPointerException) {
                    tv_scanner_empty.text = "Код с карты не считан"
                    tv_scanner_empty.visibility = View.VISIBLE
                }
            } else {
                tv_scanner_empty.text = "Необходимо ввести название карты и указать категорию"
                tv_scanner_empty.visibility = View.VISIBLE
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            activity.runOnUiThread {
                Log.e("Main", "Camera error: ${it.message}")
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
        //
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermissions(){
        val permission = ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireActivity(), "Необходимо разрешение для камеры",Toast.LENGTH_SHORT).show()
                } else{

                }
            }
        }
    }

    //выпадающее меню категорий
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
            binding.edittextCardNameScanner.isFocusable = false
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
    //

}