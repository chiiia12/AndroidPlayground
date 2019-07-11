package com.github.chiiia12.rxjavasample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import timber.log.Timber

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        Timber.plant(Timber.DebugTree())
        val observable = Observable.create<String> {
            Timber.d("onNext aaa")
            it.onNext("aaa")
            Timber.d("onNext bbb")
            it.onNext("bbb")
            Timber.d("onComplete")
            it.onComplete()
        }

        observable.doOnSubscribe {
            Timber.d("doOnSubscribe ")
        }.subscribe {
            Timber.d("subscribe $it")
        }

        Single.fromObservable(observable).subscribe(object : SingleObserver<String> {
            override fun onSuccess(t: String) {
                Timber.d("")
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
            }

        })


    }
}
