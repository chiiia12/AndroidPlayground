package com.github.chiiia12.jetpacksample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.github.chiiia12.jetpacksample.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityListBinding>(this, R.layout.activity_list)
        val viewModel = ViewModelProviders.of(this).get(TodoListViewModel::class.java)
        val adapter = TodoListAdapter()

    }
}