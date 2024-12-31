package com.alvinchych.blogpost

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.alvinchych.blogpost.databinding.ActivityMainBinding
import com.alvinchych.blogpost.ui.FragmentTestActivity
import com.alvinchych.blogpost.ui.ShowBlogActivityA
import com.alvinchych.blogpost.ui.ShowBlogActivityB
import com.alvinchych.blogpost.ui.ShowBlogActivityC
import com.alvinchych.blogpost.ui.ShowBlogActivityD
import com.alvinchych.blogpost.ui.TouchEventTestActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.showA.setOnClickListener {
            val intent = Intent(this, ShowBlogActivityA::class.java)
            startActivity(intent)
        }

        binding.showB.setOnClickListener {
            val intent = Intent(this, ShowBlogActivityB::class.java)
            startActivity(intent)
        }

        binding.showC.setOnClickListener {
            val intent = Intent(this, ShowBlogActivityC::class.java)
            startActivity(intent)
        }

        binding.showD.setOnClickListener {
            val intent = Intent(this, ShowBlogActivityD::class.java)
            startActivity(intent)
        }

        binding.touchTest.setOnClickListener {
            val intent = Intent(this, TouchEventTestActivity::class.java)
            startActivity(intent)
        }

        binding.fragmentTest.setOnClickListener {
            val intent = Intent(this, FragmentTestActivity::class.java)
            intent.putExtra(FragmentTestActivity.ACTIVITY_PARAM_COUNT, 0)
            startActivity(intent)
        }
    }
}