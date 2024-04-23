package com.alvinchych.blogpost.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.alvinchych.blogpost.databinding.FragmentTestBinding
import com.alvinchych.blogpost.viewmodel.BlogViewModel
import com.alvinchych.blogpost.viewmodel.FragmentTestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentTestBinding
    private val viewModel: FragmentTestViewModel by activityViewModels()  // share activity's viewmodel
//    private val viewModel: FragmentTestViewModel by viewModels() // create the independent viewmodel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.d(TestFragment::class.simpleName, "onCreate: $param1")
        viewModel.print()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TestFragment::class.simpleName, "onCreateView: $param1")
        // Inflate the layout for this fragment
        binding = FragmentTestBinding.inflate(inflater, container, false)
        binding.tag.text = param1
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TestFragment::class.simpleName, "onStart: $param1")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TestFragment::class.simpleName, "onResume: $param1")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TestFragment::class.simpleName, "onPause: $param1")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TestFragment::class.simpleName, "onStop: $param1")
    }

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}