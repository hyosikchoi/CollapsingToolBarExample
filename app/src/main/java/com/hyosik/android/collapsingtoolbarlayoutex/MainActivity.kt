package com.hyosik.android.collapsingtoolbarlayoutex

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.AppBarLayout
import com.hyosik.android.collapsingtoolbarlayoutex.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private var binding : ActivityMainBinding ?= null

    /** MotionLayout 시작 및 정지 유무 변수 */
    private var isGateringMotionAnimating : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)
        binding = mainActivityBinding
        initViewSettings(mainActivityBinding)
        initAppBar(mainActivityBinding)
        initScrollView(mainActivityBinding)
        initMotionLayout(mainActivityBinding)

        /** descriptionTextview 중간 text color 변경 */
        val descriptionText = mainActivityBinding.descriptionTextview.text.toString()
        val ssb  = SpannableStringBuilder(descriptionText)
        ssb.setSpan(ForegroundColorSpan(Color.parseColor("#FF0000")), 0 , 8 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mainActivityBinding.descriptionTextview.setText(ssb)
    }

    private fun initScrollView(mainActivityBinding: ActivityMainBinding) {

        mainActivityBinding.nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            /** 10dp 이상 스크롤이 일어나면 */
            if(scrollY > 5f.dpToPx(this).toInt()) {
                /** motion animate 가 진행중이지 않다면 */
                if(isGateringMotionAnimating.not()) {
                   mainActivityBinding.motionLayout.transitionToEnd()
                   mainActivityBinding.buttonShownMotionLayout.transitionToEnd()
                }
            }
            /** 반대로 10dp 안쪽으로 들어왔다면 */
            else {
                /** motion animate 가 진행중이지 않다면 */
                if(isGateringMotionAnimating.not()) {
                    mainActivityBinding.motionLayout.transitionToStart()
                    mainActivityBinding.buttonShownMotionLayout.transitionToStart()
                }
            }
        }

    }

    private fun initMotionLayout(mainActivityBinding: ActivityMainBinding) {

        mainActivityBinding.motionLayout.setTransitionListener(object: MotionLayout.TransitionListener{
            /** motion animate 가 시작 됬다면 */
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                /** 시작 및 정지 유무 변수를 시작으로 */
                isGateringMotionAnimating = true

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {}
            /** motion animate 가 끝났다면 */
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                /** 시작 및 정지 유무 변수를 정지로 */
                isGateringMotionAnimating = false
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {}
        })

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
            mainActivityBinding.toolbarBackgroundView.alpha = percentage

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
