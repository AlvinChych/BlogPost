package com.alvinchych.blogpost.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewModelScope
import com.alvinchych.blogpost.R
import com.alvinchych.blogpost.databinding.ActivityFragmentTestBinding
import com.alvinchych.blogpost.viewmodel.FragmentTestViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentTestActivity : FragmentActivity() {

    private lateinit var binding: ActivityFragmentTestBinding
    private val viewModel: FragmentTestViewModel by viewModels()

    private var addCount = 0
    private var replaceCount = 0

    private val tag: String by lazy {
        "${FragmentTestActivity::class.simpleName}:${intent.getIntExtra(ACTIVITY_PARAM_COUNT, -1)}"
    }

    private val nextActivityNum: Int by lazy {
        intent.getIntExtra(ACTIVITY_PARAM_COUNT, -1) + 1
    }

    companion object {
        const val ACTIVITY_PARAM_COUNT = "activity_param_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.print()
        Log.d(FragmentTestActivity::class.simpleName, "$tag: onCreate")

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
//        if (savedInstanceState != null) {
//            return
//        } else {
//            val firstFragment = TestFragment.newInstance("", "")
//            supportFragmentManager.beginTransaction().add(R.id.fragment_container, firstFragment, ).commit()
//        }

        binding.addFragment.setOnClickListener {
            val tag = "add:${addCount++}"
            val firstFragment = TestFragment.newInstance(tag, "")
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, firstFragment)
                .addToBackStack(tag).commit()
        }

        binding.replaceFragment.setOnClickListener {
            val tag = "replace:${replaceCount++}"
            val firstFragment = TestFragment.newInstance(tag, "")
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, firstFragment)
                .addToBackStack(tag).commit()
        }

        binding.addActivity.setOnClickListener {
            val intent = Intent(this, FragmentTestActivity::class.java)
            intent.putExtra(ACTIVITY_PARAM_COUNT, nextActivityNum)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(FragmentTestActivity::class.simpleName, "$tag: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(FragmentTestActivity::class.simpleName, "$tag: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(FragmentTestActivity::class.simpleName, "$tag: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(FragmentTestActivity::class.simpleName, "$tag: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(FragmentTestActivity::class.simpleName, "$tag: onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(FragmentTestActivity::class.simpleName, "$tag: onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(FragmentTestActivity::class.simpleName, "$tag: onRestoreInstanceState")
    }

}