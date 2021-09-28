package com.example.appbarlayoutcoordinatorlayoutdemo

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class AppBarLayoutAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_layout)

        val appBar = findViewById<AppBarLayout>(R.id.appBar)
        val title = findViewById<TextView>(R.id.title)

        appBar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                when (state) {
                    //開起
                    State.EXPANDED -> {
                        title.text = "Title Bar"
                        title.setBackgroundColor(Color.parseColor("#0900FF"))
                    }
                    //合起
                    State.COLLAPSED -> {
                        title.text = "Finish"
                        title.setBackgroundColor(Color.parseColor("#56F569"))
                    }
                    //正在滑動中
                    else -> {
                        title.setBackgroundColor(Color.parseColor("#E94B25"))
                    }
                }
            }

            override fun offset(appBarLayout: AppBarLayout, verticalOffset: Int) {
                val totalScrollRange = appBarLayout.totalScrollRange
                val ratio = abs(verticalOffset.toFloat() / totalScrollRange)
                //旋轉
                title.rotation = ratio * 360
            }
        })
    }
}