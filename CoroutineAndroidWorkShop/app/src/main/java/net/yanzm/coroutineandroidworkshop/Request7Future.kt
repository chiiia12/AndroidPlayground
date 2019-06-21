package net.yanzm.coroutineandroidworkshop

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

fun loadContributorsConcurrentAsync(req: RequestData): CompletableFuture<List<User>> = GlobalScope.future {
    loadContributorsConcurrent(req)
}
