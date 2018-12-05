package net.yanzm.coroutineandroidworkshop

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

fun loadContributorsCallbacks(req: RequestData, callback: (List<User>) -> Unit) {
    val service = createGitHubService(req.username, req.password)
    service.listOrgRepos(req.org).responseCallback { repos ->
        fun loadRepo(index: Int, out: MutableList<User>) {
            if (index < repos.size) {
                val repo = repos[index]
                service.listRepoContributors(req.org, repo.name).responseCallback { users ->
                    out.addAll(users)
                    loadRepo(index + 1, out)
                }
            } else {
                callback(out.aggregate())
            }
        }
        loadRepo(0, mutableListOf())
        //repositoryごとにcontributors一覧取得
        //合算しaggregate
    }
}

inline fun <T> Call<T>.responseCallback(crossinline callback: (T) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            checkResponse(response)
            callback(response.body()!!)
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Timber.e(t)
        }
    })
}
