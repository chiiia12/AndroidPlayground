package com.github.chiiia12.livedatasample

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.chiiia12.livedatasample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: NameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.setLifecycleOwner(this)
        viewModel = NameViewModel()
        binding.vm = viewModel

        var count = 0
        binding.button.setOnClickListener {
            count++
            viewModel.currentName.value = count.toString()
        }
    }
}
