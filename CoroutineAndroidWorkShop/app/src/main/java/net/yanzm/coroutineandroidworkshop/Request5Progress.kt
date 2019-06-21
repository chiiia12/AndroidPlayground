package net.yanzm.coroutineandroidworkshop

import timber.log.Timber

suspend fun loadContributorsProgress(req: RequestData, callback: (List<User>) -> Unit) {
//    1.serviseを取得
//    2, repositoryの一覧を取得する
//    3.各repositoryごとにcontributer一覧を取得
//    4.全contributorsを合算してaggregateし、callback(users)を呼ぶ。

    val service = createGitHubService(req.username, req.password)

    Timber.i("Loading ${req.org} repos")

    val repos = service.listOrgRepos(req.org).await()

    Timber.i("${req.org}: loaded ${repos.size} repos")

    val contribs = mutableListOf<User>()
    for (repo in repos) {
        val users = service.listRepoContributors(req.org, repo.name).await()
        contribs.addAll(users)

        Timber.i("Total: ${contribs.size} contributors")
        callback(contribs.aggregate())
    }
}
