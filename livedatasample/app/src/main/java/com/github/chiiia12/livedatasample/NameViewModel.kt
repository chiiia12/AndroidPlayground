package com.github.chiiia12.livedatasample

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class NameViewModel : ViewModel() {
    var currentName: MutableLiveData<String> = MutableLiveData()
}