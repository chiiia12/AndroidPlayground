package com.github.chiiia12.rxjavasample

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

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
