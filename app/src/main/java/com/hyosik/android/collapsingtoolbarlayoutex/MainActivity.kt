package com.hyosik.android.collapsingtoolbarlayoutex

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.AppBarLayout
import com.hyosik.android.collapsingtoolbarlayoutex.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        binding = mainActivityBinding
        initViewSettings(mainActivityBinding)
        initAppBar(mainActivityBinding)


    }

    private fun initViewSettings(mainActivityBinding: ActivityMainBinding) {

        window.statusBarColor = Color.TRANSPARENT

        ViewCompat.setOnApplyWindowInsetsListener(mainActivityBinding.coordinator) { v : View, insets : WindowInsetsCompat ->

            mainActivityBinding.toolbarContainer.layoutParams = (mainActivityBinding.toolbarContainer.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(0 , insets.systemWindowInsetTop, 0, 0)
            }

            insets.consumeSystemWindowInsets()
        }

    }

    private fun initAppBar(mainActivityBinding: ActivityMainBinding) {

        mainActivityBinding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            /** toolBar의 alpha값이 변하기 시작하는 offset기준값 */
            val topPadding = 250f.dpToPx(this)

            /** scroll 절대값 */
            val abstractOffset = abs(verticalOffset)

            /** alpha 값이 변하는 scroll 전체범위 */
            val realAlphaScrollHeight = appBarLayout.totalScrollRange - topPadding

            /** alpha 값이 변하는 scroll 값 */
            /** scroll 한 절대값 - 변하기 시작하는 scroll 기준값  */
            val realAlphaVerticalOffset = if (abstractOffset < topPadding) 0f else abstractOffset - topPadding

            /** offset 기준값에 scroll 절대값이 못 미치면 toolBar 는 계속 투명하게 하고 return */
            if (abstractOffset < topPadding) {
                mainActivityBinding.toolbarBackgroundView.alpha = 0f
                return@OnOffsetChangedListener
            }

            /** offset 기준값 을 넘었다면 alpha 값의 percentage 를 구한다. */
            /** 현재 alpha 값이 변하는 scroll / 전체범위 */
            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight

            //Log.i("percentage" , "percent : ${percentage}")

            /** toolBar background 의 alpha 값이 percentage가 커짐에 따라 0 에서 1로 바뀌어야 하므로 */
            /** 1 - percentage 는 1 에서 0으로 바뀌는 것이므로 반대로 1 - (1- percentage) 로 해준다. */
            mainActivityBinding.toolbarBackgroundView.alpha = 1 - (1 - percentage)

        })

        initActionBar(mainActivityBinding)

    }

    private fun initActionBar(mainActivityBinding: ActivityMainBinding) {
        mainActivityBinding.apply {
            toolbar.navigationIcon = null
            /** toolBar 좌우 여백 없이 꽉 차게끔 설정 */
            toolbar.setContentInsetsAbsolute(0, 0)
            setSupportActionBar(toolbar)
            supportActionBar?.let {
                it.setHomeButtonEnabled(false)
                it.setDisplayHomeAsUpEnabled(false)
                it.setDisplayShowHomeEnabled(false)
            }
        }

    }
}
