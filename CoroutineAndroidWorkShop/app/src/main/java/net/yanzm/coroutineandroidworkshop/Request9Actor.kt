package net.yanzm.coroutineandroidworkshop

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import timber.log.Timber

/**
 * 自身のchannelに来たlist<User>をaggregateして
 * uiUpdateActor にsendしている
 */
fun CoroutineScope.aggregatorActor(
    uiUpdateActor: SendChannel<List<User>>
) = actor<List<User>> {
    var contribs: List<User> = emptyList() // STATE
    for (users in channel) {
        contribs = (contribs + users).aggregate()
        uiUpdateActor.send(contribs)
    }
}

class WorkerRequest(
    val service: GitHubService,
    val org: String,
    val repo: String
)

//requests channelに流れてくるreqを受け取って、contributorsを取得し結果をaggregatorにsend()している
fun CoroutineScope.workerJob(
    requests: ReceiveChannel<WorkerRequest>,
    aggregator: SendChannel<List<User>>
): Job = launch {
    for (req in requests) {
        val users = req.service.listRepoContributors(req.org, req.repo).await()
        aggregator.send(users)
    }
}

suspend fun loadContributorsActor(req: RequestData, uiUpdateActor: SendChannel<List<User>>) = coroutineScope<Unit> {
    val service = createGitHubService(req.username, req.password)

    Timber.i("Loading ${req.org} repos")

    val repos = service.listOrgRepos(req.org).await()

    Timber.i("${req.org}: loaded ${repos.size} repos")

    val aggregator = aggregatorActor(uiUpdateActor)

    val requests = Channel<WorkerRequest>()
    val workers = List(4) {
        workerJob(requests, aggregator)
    }
    //reposの数だけrequests channelにworkerRequestを流す
    // →workerJobの中の処理でrequests channelにreqが流れてくるので、contributorsの取得処理が走る
    //aggregatorにList<User>が流れてくるので、aggregatorActorの中の処理でuiUpdateActorにaggregateされたList<User>がsendされる
    for (repo in repos) {
        requests.send(WorkerRequest(service, req.org, repo.name))
    }
    requests.close()
    workers.joinAll()
    //aggregatorをclose()するにはcontributorsの取得処理が完了したあとでないといけないので
    //そのまえに、workers.joinAll()する必要がある
    aggregator.close()
}
