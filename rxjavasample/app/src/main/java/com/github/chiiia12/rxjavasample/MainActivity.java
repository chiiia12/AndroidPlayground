package com.github.chiiia12.rxjavasample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.chiiia12.rxjavasample.databinding.ContributorsListItemBinding;
import com.github.chiiia12.rxjavasample.network.GitHubService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    //TODO delete application
    private static final String CLIENT_ID = "87f2b0be5f1b6a168c51";
    private static final String CLIENT_SECRET = "097e6216c89451fc03598c41d51f6d5ccbb2e66c";
    private EditText ownerEditText;
    private EditText repositoryEditText;
    private ContributorsAdapter adapter;
    private GitHubService service;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ownerEditText = findViewById(R.id.owner);
        repositoryEditText = findViewById(R.id.repository);
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContributorsAdapter(this);
        recyclerView.setAdapter(adapter);

        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        final OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addNetworkInterceptor(new GitHubOAuthAppAuthenticationInterceptor(CLIENT_ID, CLIENT_SECRET))
                .build();

        service = new Retrofit.Builder().baseUrl("https://api.github.com")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(GitHubService.class);

        final Button loadButton = findViewById(R.id.load);
        loadButton.setOnClickListener(view -> onLoadClicked());
    }

    private void onLoadClicked() {
        disposable = service.contributors(ownerEditText.getText().toString(), repositoryEditText.getText().toString())
                .subscribeOn(Schedulers.io())
                .flatMap(Observable::fromIterable)
                .take(5)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> adapter.setContributors(ContributorsListItem.fromJson(data)),
                        err -> Log.e(TAG, err.getLocalizedMessage()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private static class ContributorsAdapter
            extends RecyclerView.Adapter<ContributorsListItemViewHolder> {
        private final List<ContributorsListItem> contributors;
        private final LayoutInflater inflater;

        private ContributorsAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            contributors = new ArrayList<>();
        }

        @Override
        public ContributorsListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ContributorsListItemViewHolder(
                    ContributorsListItemBinding.inflate(inflater, parent, false));
        }

        @Override
        public void onBindViewHolder(ContributorsListItemViewHolder holder, int position) {
            holder.bind(contributors.get(position));
        }

        @Override
        public int getItemCount() {
            return contributors.size();
        }

        public void setContributors(List<ContributorsListItem> contributors) {
            this.contributors.clear();
            this.contributors.addAll(contributors);
            notifyDataSetChanged();
        }
    }

    private static class ContributorsListItemViewHolder extends RecyclerView.ViewHolder {
        private final ContributorsListItemBinding binding;

        public ContributorsListItemViewHolder(ContributorsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ContributorsListItem contributor) {
            binding.setContributor(contributor);
            binding.executePendingBindings();
        }
    }
}
