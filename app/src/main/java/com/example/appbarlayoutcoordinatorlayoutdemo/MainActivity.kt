package com.example.appbarlayoutcoordinatorlayoutdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    lateinit var layout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun collapsing(view: View) {
        startActivity(Intent(this, CollapsingToolbarAct::class.java))
    }

    fun appBar(view: View) {
        startActivity(Intent(this, AppBarLayoutAct::class.java))
    }
}