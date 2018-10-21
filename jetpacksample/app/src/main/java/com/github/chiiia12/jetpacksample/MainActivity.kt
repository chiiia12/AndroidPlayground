package com.github.chiiia12.jetpacksample

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.chiiia12.jetpacksample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = TodoViewModel()
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        binding.doneButton.setOnClickListener {
            viewModel.done()
        }
    }
}
