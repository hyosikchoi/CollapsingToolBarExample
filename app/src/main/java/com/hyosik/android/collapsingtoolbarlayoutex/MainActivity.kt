package com.hyosik.android.collapsingtoolbarlayoutex

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hyosik.android.collapsingtoolbarlayoutex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        binding = mainActivityBinding
        initViewSettings(mainActivityBinding)

    }

    private fun initViewSettings(mainActivityBinding: ActivityMainBinding) {

        window.statusBarColor = Color.TRANSPARENT

        ViewCompat.setOnApplyWindowInsetsListener(mainActivityBinding.coordinator) { v : View, insets : WindowInsetsCompat ->
            insets.consumeSystemWindowInsets()
        }

    }
}