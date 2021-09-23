package com.example.clickstask.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.clickstask.R
import com.example.clickstask.Utils
import com.example.clickstask.ui.model.ArticlesItem
import kotlinx.android.synthetic.main.item_news.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(
    private val articlesList: ArrayList<ArticlesItem>,
    private val onArticleClickCallback: (article: ArticlesItem) -> Unit
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(), Filterable {

    class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView)

    var articles: List<ArticlesItem>? = articlesList

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = articles!![position]

        holder.itemView.txtTitle.text = item.title

        holder.itemView.txtDate.text = Utils().formatDate(item.publishedAt!!)
        holder.itemView.txtSourceName.text = item.source!!.name

        Glide.with(holder.itemView)
            .load(item.urlToImage)
            .into(holder.itemView.imgNews)

        holder.itemView.articleCard.setOnClickListener {
            onArticleClickCallback.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return if (articles == null) 0 else articles!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    articles = articlesList
                } else {
                    val resultList = ArrayList<ArticlesItem>()
                    for (row in articlesList) {
                        if (row.source!!.name!!.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    articles = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = articles
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                articles = results?.values as ArrayList<ArticlesItem>
                notifyDataSetChanged()
            }
        }
    }

}
