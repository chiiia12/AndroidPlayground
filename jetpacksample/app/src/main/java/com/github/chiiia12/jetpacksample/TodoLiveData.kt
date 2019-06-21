package com.github.chiiia12.jetpacksample

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable

class TodoLiveData : MutableLiveData<TodoBindingModel>() {
    private val disposable = CompositeDisposable()
    override fun onActive() {
        super.onActive()
        value = TodoBindingModel(1L, "name", "description", false)
    }

    override fun onInactive() {
        disposable.clear()
        super.onInactive()
    }
}