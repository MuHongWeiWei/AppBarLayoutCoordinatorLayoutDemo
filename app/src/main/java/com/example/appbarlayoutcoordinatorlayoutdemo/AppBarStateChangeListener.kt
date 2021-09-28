package com.example.appbarlayoutcoordinatorlayoutdemo

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlin.math.abs


/**
 * Author: Wade
 * E-mail: tony91097@gmail.com
 * Date: 2021/9/28
 */
abstract class AppBarStateChangeListener : OnOffsetChangedListener {

    sealed class State {
        object EXPANDED : State()
        object COLLAPSED : State()
        object IDLE : State()
    }

    private var mCurrentState: State = State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        when {
            verticalOffset == 0 -> {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED)
                }
                mCurrentState = State.EXPANDED
            }
            abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED)
                }
                mCurrentState = State.COLLAPSED
            }
            else -> {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE)
                }
                mCurrentState = State.IDLE
            }
        }
        offset(appBarLayout, verticalOffset)
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)
    abstract fun offset(appBarLayout: AppBarLayout, verticalOffset: Int)
}