package com.github.chiiia12.rxjavasample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import timber.log.Timber

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Timber.plant(Timber.DebugTree())

    }
}
