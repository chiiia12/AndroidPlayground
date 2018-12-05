package net.yanzm.coroutineandroidworkshop

import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine { cont ->
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                //一時停止していたものが再開する
                cont.resume(response.body()!!)
            } else {
                cont.resumeWithException(ErrorResponse(response))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            cont.resumeWithException(t)
        }
    })
    cont.invokeOnCancellation {
        //retrofitの方のcancelでちゃんとAPI通信もcancelするようにしておこう
        cancel()
    }
}

class ErrorResponse(response: Response<*>) : Exception(
    "Failed with ${response.code()}: ${response.message()}\n${response.errorBody()?.string()}"
)
