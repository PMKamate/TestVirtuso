package com.practicaltest.myapplication.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
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


class NewsPageAdapter(private val listener: NewsItemListener, val context: Context?) :
    PagedListAdapter<News, NewsViewHolder1>(DIFF_CALLBACK) {

    interface NewsItemListener {
        fun onClickedNews(news: News)
    }

    // val items = ArrayList<News>()

//    fun setItems(items: ArrayList<News>) {
//        this.items.clear()
//        this.items.addAll(items)
//        notifyDataSetChanged()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder1 {
        val binding: ItemBinding =
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder1(binding, listener, context)
    }

    override fun onBindViewHolder(holder: NewsViewHolder1, position: Int) =
        holder.bind(newsPosition(position), context)
    //  val friend: News? = newsPosition(position)
    //holder.friend.text = "" + friend?.id.toString() + "--" + friend?.title


    fun newsPosition(pos: Int): News? {
        return getItem(pos)
    }
//    override fun getItemCount(): Int = items.size
//
//    override fun onBindViewHolder(holder: NewsViewHolder1, position: Int) =
//        holder.bind([position],context)

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<News> =
            object : DiffUtil.ItemCallback<News>() {
                override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                    return oldItem.id == newItem.id
                }
            }
    }
}

class NewsViewHolder1(
    private val itemBinding: ItemBinding,
    private val listener: NewsPageAdapter.NewsItemListener,
    context: Context?
) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var news: News

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("CheckResult")
    fun bind(item: News?, context: Context?) {
        item?.let { item1 ->
            this.news = item1
            itemBinding.title.text = item1.title
            itemBinding.desc.text = item1.description
            itemBinding.author.text = item1.publishedAt

            val requestOptions = RequestOptions()
            requestOptions.placeholder(Utils.getRandomDrawbleColor())
            requestOptions.error(Utils.getRandomDrawbleColor())
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
            requestOptions.centerCrop()
            context?.let {
                item1.urlToImage?.let {url->
                    Glide.with(it)
                        .load(url)
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


        }

    }

    override fun onClick(v: View?) {
        listener.onClickedNews(news)
    }

}


