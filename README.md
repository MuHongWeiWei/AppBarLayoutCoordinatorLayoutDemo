# Android AppBarLayout與CoordinatorLayout的華麗搭配

##### AppBarLayout與CoordinatorLayout兩個搭配可以變出不同的樣子，這裡會示範比較常用的操作來搭到特殊的效果，只要合理運用兩個Layout的滑動就可以很順利的達到你要的效果，可以配合RecyclerView或ScrollerView或NestedScrollView

---

#### 文章目錄
<ol>
	<li><a href="#a">相關屬性</a></li>
    <li><a href="#b">AppBarLayout畫面布局</a></li>
    <li><a href="#c">創建滑動監聽</a></li>
    <li><a href="#d">自定義滑動效果</a></li>
    <li><a href="#e">CollapsingToolbarLayout畫面布局</a></li>
    <li><a href="#f">效果展示</a></li>
	<li><a href="#g">Github</a></li>
</ol>

---

<a id="a"></a>
#### 1.相關屬性
##### AppBarLayout
```Kotlin
//可滾動
app:layout_scrollFlags="scroll"
//先滾動自己在滾動子View
app:layout_scrollFlags="enterAlways"
//先滾動自己到最小高度其他View滾完再滾到最大高度
app:layout_scrollFlags="enterAlwaysCollapsed"
//先滾動自己到最小高度
app:layout_scrollFlags="exitUntilCollapsed"
//滾動依照方向會有吸附作用
app:layout_scrollFlags="snap"
app:layout_behavior
```
##### CoordinatorLayout
```Kotlin
//固定讓View在AppBar下方
app:layout_behavior="@string/appbar_scrolling_view_behavior"
```

##### CollapsingToolbarLayout
```Kotlin
//收起後的顏色
app:contentScrim="@color/white"
//收起後的位子
app:collapsedTitleGravity="center"
//開啟後的位子
app:expandedTitleGravity="center"
```


#### 2.AppBarLayout畫面布局
```XML
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/appBar"
        android:background="#56F569"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:background="#56F569"
            android:gravity="center"
            android:text="Banner頁"
            android:textColor="@color/white"
            android:textSize="26sp"
            app:layout_scrollFlags="scroll" />

        <TextView
            android:id="@+id/title"
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:textSize="20sp"
            android:textStyle="bold"
            android:background="#0900FF"
            android:gravity="center"
            android:text="Title Bar"
            android:textColor="@color/white" />

    </com.google.android.material.appbar.AppBarLayout>

    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">

        <LinearLayout
            android:id="@+id/ll_bottom"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <TextView
                android:layout_width="match_parent"
                android:layout_height="500dp"
                android:gravity="center"
                android:text="下面還可以繼續滑動~" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#009988"
                android:gravity="center"
                android:text="AAA" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#550028"
                android:gravity="center"
                android:text="BBB" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#009922"
                android:gravity="center"
                android:text="CCC" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#004312"
                android:gravity="center"
                android:text="DDD" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#F29922"
                android:gravity="center"
                android:text="EEE" />
        </LinearLayout>
    </androidx.core.widget.NestedScrollView>
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

<a id="b"></a>
#### 3.創建滑動監聽
```Kotlin
package com.example.screenutils

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
```

<a id="d"></a>
#### 4.自定義滑動效果
```Kotlin
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
```

<a id="e"></a>
#### 5.CollapsingToolbarLayout畫面布局
```XML
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="false"
    tools:context=".MainActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/app_bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <com.google.android.material.appbar.CollapsingToolbarLayout
            android:background="#56F569"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:contentScrim="@color/white"
            app:layout_scrollFlags="scroll|exitUntilCollapsed"
            app:title="ToolbarLayout">

            <ImageView
                android:id="@+id/v_pager_logo"
                android:layout_width="match_parent"
                android:layout_height="300dp" />

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="50dp" />
        </com.google.android.material.appbar.CollapsingToolbarLayout>

    </com.google.android.material.appbar.AppBarLayout>

    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">

        <LinearLayout
            android:id="@+id/ll_bottom"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <TextView
                android:layout_width="match_parent"
                android:layout_height="500dp"
                android:gravity="center"
                android:text="下面還可以繼續滑動~" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#009988"
                android:gravity="center"
                android:text="AAA" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#550028"
                android:gravity="center"
                android:text="BBB" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#009922"
                android:gravity="center"
                android:text="CCC" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#004312"
                android:gravity="center"
                android:text="DDD" />

            <TextView
                android:layout_width="match_parent"
                android:layout_height="50dp"
                android:background="#F29922"
                android:gravity="center"
                android:text="EEE" />
        </LinearLayout>
    </androidx.core.widget.NestedScrollView>
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```


<a id="f"></a>
#### 6.效果展示

<a href="https://badgameshow.com/fly/wp-content/uploads/2021/09/video-1632806291.gif"><img src="https://badgameshow.com/fly/wp-content/uploads/2021/09/video-1632806291.gif" width="50%"/></a>

<a id="g"></a>
#### 7.Github
[Android AppBarLayout與CoordinatorLayout的華麗搭配 Github](https://github.com/MuHongWeiWei/AppBarLayoutCoordinatorLayoutDemo)
