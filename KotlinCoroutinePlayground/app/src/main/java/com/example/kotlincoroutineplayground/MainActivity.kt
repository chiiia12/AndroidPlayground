package com.example.kotlincoroutineplayground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private val uiScope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.plant(Timber.DebugTree())
        Timber.d("coroutine start")
        uiScope.launch {
            runCatching {
                val completeFirst = async { completeFirst() }
                val completeSecond = async { completeSecond() }
                Timber.d("==================")
                Timber.d("${completeFirst.await()} ${completeSecond.await()}")
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    private suspend fun completeFirst(): String {
        delay(2000)
        Timber.d("completeFirst")
        return "completeFirst"
    }

    private suspend fun completeSecond(): String {
        delay(1000)
        Timber.d("completeSecond")
        throw Throwable("test throwable")
        return "completeSecond"
    }
}
