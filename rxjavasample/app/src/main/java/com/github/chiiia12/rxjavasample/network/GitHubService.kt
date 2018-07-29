package com.github.chiiia12.rxjavasample.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("/repos/{owner}/{repo}/contributors")
    fun contributors(@Path("owner") owner: String,
                     @Path("repo") repo: String): Observable<List<Contributor>>

    @GET("/users/{user}")
    fun user(@Path("user") userId: String): Observable<User>
}
