package com.github.chiiia12.livedatasample

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.chiiia12.livedatasample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: NameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.vm = NameViewModel()
        viewModel = ViewModelProviders.of(this).get(NameViewModel::class.java)
        val nameObserver = Observer<String> {

            binding.textview.text = it
        }
        viewModel.currentName.observe(this, nameObserver)

        var count = 0
        binding.button.setOnClickListener {
            count++
            viewModel.currentName.value = count.toString()
        }
    }
}
