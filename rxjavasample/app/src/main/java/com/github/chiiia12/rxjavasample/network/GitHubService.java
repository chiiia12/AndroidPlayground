package com.github.chiiia12.rxjavasample.network;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
  @GET("/repos/{owner}/{repo}/contributors")
  public Observable<List<Contributor>> contributors(@Path("owner") String owner,
                                                    @Path("repo") String repo);

  @GET("/users/{user}")
  public Observable<User> user(@Path("user") String userId);
}
