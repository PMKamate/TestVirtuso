package com.practicaltest.myapplication.ui.main

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicaltest.myapplication.R
import com.practicaltest.myapplication.adapter.NewsAdapter
import com.practicaltest.myapplication.data.entities.News
import com.practicaltest.myapplication.databinding.MainFragmentBinding
import com.practicaltest.myapplication.details.DetailsActivity
import com.practicaltest.myapplication.utils.Resource
import com.practicaltest.myapplication.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(), NewsAdapter.NewsItemListener {
    private var binding: MainFragmentBinding by autoCleared()
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: NewsAdapter
    companion object {
        fun newInstance() = MainFragment()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = NewsAdapter(this, context)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.newsDataSource.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (!it.data.isNullOrEmpty()) {
                        Log.d("TAG: ", "news: " + it.data.size)
                        adapter.setItems(ArrayList(it.data))
                    }
                }
                Resource.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    override fun onClickedNews(nesId: Int) {
        val intent = Intent(activity, DetailsActivity::class.java)
        val model: News = adapter.items.get(nesId)
        intent.putExtra("id", nesId)
        intent.putExtra("tag", "NewsApiFragment")
        intent.putExtra("url", model.url)
        intent.putExtra("title", model.title)
        intent.putExtra("img", model.urlToImage)
        intent.putExtra("date", model.publishedAt)
        intent.putExtra("source", model.author)
        intent.putExtra("author", model.author)
        startActivity(intent)
    }
}