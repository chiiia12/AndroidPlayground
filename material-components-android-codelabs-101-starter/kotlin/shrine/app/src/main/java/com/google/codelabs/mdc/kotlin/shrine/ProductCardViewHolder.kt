package com.google.codelabs.mdc.kotlin.shrine

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.android.volley.toolbox.NetworkImageView
import kotlinx.android.synthetic.main.shr_product_card.view.*

class ProductCardViewHolder(itemView: View) //TODO: Find and store views from itemView
    : RecyclerView.ViewHolder(itemView) {
    var productImage: NetworkImageView = itemView.product_image
    var productTitle: TextView = itemView.product_title
    var productPrice: TextView = itemView.product_price
}
