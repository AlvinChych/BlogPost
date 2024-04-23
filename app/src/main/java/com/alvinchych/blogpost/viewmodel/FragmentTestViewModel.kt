package com.alvinchych.blogpost.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

class FragmentTestViewModel : ViewModel() {

    init {
        Log.d(this::class.java.toString(), "fragment viewmodel created")
    }

    fun print() {
        println("${this::class.simpleName} $this")
    }
}