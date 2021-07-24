package com.practicaltest.myapplication.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.practicaltest.myapplication.data.entities.News
import com.practicaltest.myapplication.databinding.ItemBinding
import com.practicaltest.myapplication.utils.Utils


class NewsAdapter(private val listener: NewsItemListener,val context: Context?) : RecyclerView.Adapter<NewsViewHolder>() {

    interface NewsItemListener {
        fun onClickedNews(nesId: Int)
    }

    val items = ArrayList<News>()

    fun setItems(items: ArrayList<News>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding: ItemBinding =
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding, listener,context)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) =
        holder.bind(items[position],context)
}

class NewsViewHolder(
    private val itemBinding: ItemBinding,
    private val listener: NewsAdapter.NewsItemListener,
    context: Context?
) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var news: News

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("CheckResult")
    fun bind(item: News, context:Context?) {
        this.news = item
        itemBinding.title.text = item.title
        itemBinding.desc.text = item.description
        itemBinding.author.text = item.publishedAt
        Glide.with(itemBinding.root)
            .load(item.urlToImage)
            .transform(CircleCrop())
            .into(itemBinding.img)
        val requestOptions = RequestOptions()
        requestOptions.placeholder(Utils.getRandomDrawbleColor())
        requestOptions.error(Utils.getRandomDrawbleColor())
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.centerCrop()
        context?.let {
            Glide.with(it)
                .load(item.urlToImage)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .centerCrop()
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        @Nullable e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Test:exceptiom ", "" + e)
                        itemBinding.prograssLoadPhoto.setVisibility(View.GONE)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                         itemBinding.prograssLoadPhoto.setVisibility(View.GONE)
                        return false
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemBinding.img)
        }

    }

    override fun onClick(v: View?) {
        listener.onClickedNews(news.id)
    }
}


