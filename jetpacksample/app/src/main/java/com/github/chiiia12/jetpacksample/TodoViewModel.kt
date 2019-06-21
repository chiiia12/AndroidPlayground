package com.github.chiiia12.jetpacksample

import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {
    val todoLiveData = TodoLiveData()

    fun done() {
        todoLiveData.value?.let { todo ->
            if (todo.done) {
                return
            }
            //execute usecase for update done
            todoLiveData.value = todo.copy(description = "task done", done = true)
        }
    }
}