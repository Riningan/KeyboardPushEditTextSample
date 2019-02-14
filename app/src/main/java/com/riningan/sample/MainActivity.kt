package com.riningan.sample

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), ViewTreeObserver.OnGlobalLayoutListener {
    private var mTopViewHeight: Int = 0
    private var mActivityHeight: Int = 0
    private var mLayoutHeight: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        mTopViewHeight = resources.getDimensionPixelSize(R.dimen.top_view_height)
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
        val tv = TypedValue()
        val toolbarHeight = if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        } else {
            0
        }
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val usableHeight = displayMetrics.heightPixels
        val softButtonsBarHeight = if (screenHeight > usableHeight) screenHeight - usableHeight else 0
        mActivityHeight = screenHeight - statusBarHeight - softButtonsBarHeight
        mLayoutHeight = screenHeight - statusBarHeight - toolbarHeight - softButtonsBarHeight

        findViewById<View>(android.R.id.content).viewTreeObserver.addOnGlobalLayoutListener(this)
        ll.apply {
            layoutParams.height = mActivityHeight
            requestLayout()
        }
    }

    override fun onDestroy() {
        findViewById<View>(android.R.id.content).viewTreeObserver.removeOnGlobalLayoutListener(this)
        super.onDestroy()
    }

    override fun onGlobalLayout() {
        findViewById<View>(android.R.id.content).let {
            val dif = mActivityHeight - it.height
            if (dif > 150) {
                nsl.smoothScrollTo(0, mTopViewHeight)
                if (dif < mTopViewHeight) {
                    val newHeight = mLayoutHeight - dif + mTopViewHeight
                    if (newHeight != ll.layoutParams.height) {
                        ll.apply {
                            layoutParams.height = newHeight
                            requestLayout()
                        }
                    }
                }
            } else {
                if (mLayoutHeight != ll.layoutParams.height) {
                    ll.apply {
                        layoutParams.height = mLayoutHeight
                        requestLayout()
                    }
                }
            }
        }
    }
}