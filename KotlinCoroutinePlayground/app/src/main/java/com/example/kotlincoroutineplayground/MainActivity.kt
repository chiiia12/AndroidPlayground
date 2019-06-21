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
            val completeFirst = async { runCatching { completeFirst() } }
            val completeSecond = async { runCatching { completeSecond() } }
            Timber.d("==================")
            Timber.d("${completeFirst.await()} ${completeSecond.await()}")
            listOf(completeFirst.await(), completeSecond.await()).filter {
                it.isFailure
            }.let {
                if (it.isEmpty()) {
                    Timber.d("all job success")
                } else {
                    it.map { result ->
                        Timber.d("failed ${result.exceptionOrNull()}")
                    }
                }
            }

        }
    }

    private suspend fun completeFirst(): String {
        delay(2000)
        Timber.d("completeFirst")
        throw Throwable("test first")
        return "completeFirst"
    }

    private suspend fun completeSecond(): String {
        delay(1000)
        Timber.d("completeSecond")
        throw Throwable("test second")
        return "completeSecond"
    }
}
