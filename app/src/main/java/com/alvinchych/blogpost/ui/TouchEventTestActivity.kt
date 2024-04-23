package com.alvinchych.blogpost.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.core.view.children
import androidx.core.view.setMargins
import com.alvinchych.blogpost.databinding.ActivityTouchEventTestBinding

class TouchEventTestActivity : ComponentActivity() {

    private lateinit var binding: ActivityTouchEventTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTouchEventTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add Views A, B, and C to the FrameLayout with A on top
        val viewA = createView("A", Color.RED, FrameLayout.LayoutParams(200, 200).apply {
            setMargins(50)
        })
        val viewB = createView("B", Color.GREEN, FrameLayout.LayoutParams(100, 100).apply {
            setMargins(50)
        })
        val viewC = createView("C", Color.BLUE, FrameLayout.LayoutParams(50, 50).apply {
            setMargins(50)
        })

        binding.testFrameLayout.apply {
            // Add views in the reverse order so that view A is at the bottom
            addView(viewC)
            addView(viewB)
            addView(viewA)
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createView(tag: String, color: Int, params: FrameLayout.LayoutParams): View {
        return View(this).apply {
            this.tag = tag
            setBackgroundColor(color)
            layoutParams = params
            setOnTouchListener { v, event ->
                handleTouch(v, event)
            }
        }
    }

    private fun handleTouch(view: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            println("View ${view.tag} was touched")
            return true // The touch event is consumed by the view
        }
        return false
    }
}

class CustomizeFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            children.forEach { child ->
                if (isViewUnder(ev, child)) {
                    if (child.dispatchTouchEvent(ev)) {
                        return true
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isViewUnder(event: MotionEvent, child: View): Boolean {
        val rect = Rect()
        child.getHitRect(rect)
        return rect.contains(event.x.toInt(), event.y.toInt())
    }
}