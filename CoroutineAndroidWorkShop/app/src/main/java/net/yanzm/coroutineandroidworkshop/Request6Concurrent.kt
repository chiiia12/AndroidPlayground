package net.yanzm.coroutineandroidworkshop

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import timber.log.Timber

suspend fun loadContributorsConcurrent(req: RequestData): List<User> = coroutineScope {
    //呼び出し元のcoroutineScopeがthisとして使えるようになる
    val service = createGitHubService(req.username, req.password)

    Timber.i("Loading ${req.org} repos")

    val repos = service.listOrgRepos(req.org).await()

    Timber.i("${req.org}: loaded ${repos.size} repos")

    val deferredList = repos.map { repo ->
        //並列で動かしたかったらasync coroutinesを使う
        async {
            //coroutineScopeの中でないとasyncが呼べな
            service.listRepoContributors(req.org, repo.name).await()
        }
    }
    //deferred単位でcoroutineが発生する
    val users = deferredList.awaitAll()
    users.flatten().aggregate()
}
