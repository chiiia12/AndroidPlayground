package com.github.chiiia12.rxjavasample

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.github.chiiia12.rxjavasample.databinding.ContributorsListItemBinding
import com.github.chiiia12.rxjavasample.network.GitHubService
import com.github.chiiia12.rxjavasample.network.User
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.SerialDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    private var ownerEditText: EditText? = null
    private var repositoryEditText: EditText? = null
    private var adapter: ContributorsAdapter? = null
    private var service: GitHubService? = null
    private val disposable = SerialDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ownerEditText = findViewById(R.id.owner)
        repositoryEditText = findViewById(R.id.repository)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ContributorsAdapter(this)
        recyclerView.adapter = adapter

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .addNetworkInterceptor(GitHubOAuthAppAuthenticationInterceptor(CLIENT_ID, CLIENT_SECRET))
                .build()

        service = Retrofit.Builder().baseUrl("https://api.github.com")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(GitHubService::class.java)

        val loadButton = findViewById<Button>(R.id.load)
        loadButton.setOnClickListener { onLoadClicked() }
        Observable.combineLatest<Boolean, Boolean, Boolean>(
                RxTextView.textChanges(ownerEditText!!).map { s -> s.isNotEmpty() },
                RxTextView.textChanges(repositoryEditText!!).map { s -> s.isNotEmpty() },
                BiFunction { ownerNotEmpty, repositoryNotEmpty -> ownerNotEmpty!! && repositoryNotEmpty!! }).subscribe { isEnable -> loadButton.isEnabled = isEnable!! }
    }

    private fun onLoadClicked() {
        disposable.set(service!!.contributors(ownerEditText!!.text.toString(), repositoryEditText!!.text.toString())
                .subscribeOn(Schedulers.io())
                .flatMap { element -> Observable.fromIterable(element) }
                .concatMapEager<User> { contributor -> service!!.user(contributor.login) }
                .map { user -> ContributorsListItem(user.login, user.name) }
                .take(5)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data -> adapter!!.setContributors(data) },
                        { err -> Log.e(TAG, err.localizedMessage) }))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }


    class ContributorsListItemViewHolder(private val binding: ContributorsListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contributor: ContributorsListItem) {
            binding.contributor = contributor
            binding.executePendingBindings()
        }


    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        //TODO delete application
        private const val CLIENT_ID = "87f2b0be5f1b6a168c51"
        private const val CLIENT_SECRET = "097e6216c89451fc03598c41d51f6d5ccbb2e66c"
    }

    class ContributorsAdapter(context: Context) : RecyclerView.Adapter<MainActivity.ContributorsListItemViewHolder>() {
        private val contributors: MutableList<ContributorsListItem>
        private val inflater: LayoutInflater

        init {
            inflater = LayoutInflater.from(context)
            contributors = ArrayList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivity.ContributorsListItemViewHolder {
            return MainActivity.ContributorsListItemViewHolder(
                    ContributorsListItemBinding.inflate(inflater, parent, false))
        }

        override fun onBindViewHolder(holder: MainActivity.ContributorsListItemViewHolder, position: Int) {
            holder.bind(contributors[position])
        }

        override fun getItemCount(): Int {
            return contributors.size
        }

        fun setContributors(contributors: List<ContributorsListItem>) {
            this.contributors.clear()
            this.contributors.addAll(contributors)
            notifyDataSetChanged()
        }
    }
}
