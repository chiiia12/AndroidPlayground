package net.yanzm.coroutineandroidworkshop

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

//concurrentもしたいしprogressも表示したい
suspend fun loadContributorsGather(req: RequestData, callback: suspend (List<User>) -> Unit) = coroutineScope {
    //呼び出し元のcoroutineScopeがthisとして使えるようになる
    val service = createGitHubService(req.username, req.password)

    Timber.i("Loading ${req.org} repos")

    val repos = service.listOrgRepos(req.org).await()

    Timber.i("${req.org}: loaded ${repos.size} repos")

    //channel作る
    val channel = Channel<List<User>>()

    // repo
    for (repo in repos) {
        //新しいcoroutineが発行される
        launch {
            val users = service.listRepoContributors(req.org, repo.name).await()
            channel.send(users)
        }
    }
    // launchはasyncみたいにawaitAllがないのか
    var contribs = emptyList<User>()
    repeat(repos.size) {
        //channelがデータが流れてくるまで待つ
        //何回もなげつけるにはrepeatでsuspendしなきゃいけないのかな
        val users = channel.receive()
        contribs = (contribs + users).aggregate()
        callback(contribs)
    }
    Timber.i("Total: ${contribs.size} contributors")
}
