package com.example.clickstask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clickstask.MainActivity
import com.example.clickstask.R
import com.example.clickstask.databinding.FragmentHomeBinding
import com.example.clickstask.handleData.HandleError
import com.example.clickstask.ui.adapter.NewsAdapter
import com.example.clickstask.ui.model.ArticlesItem
import com.example.clickstask.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

class HomeFragment : Fragment(){

    private lateinit var viewRef: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var newsAdapter: NewsAdapter

    private val onArticleCallback: (article: ArticlesItem) -> Unit = { article ->
        homeViewModel.selectedNews = article
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewRef = FragmentHomeBinding.inflate(inflater, container, false)

        return viewRef.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.getNewsSource()

        setBtnListener()

        subscribeToHandleData()
    }

    private fun setBtnListener() {
        viewRef.refreshHome.setOnRefreshListener {
            homeViewModel.getNewsSource()
            viewRef.refreshHome.isRefreshing = false
        }

        viewRef.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newsAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun subscribeToHandleData() {
        homeViewModel.handleData.observe(viewLifecycleOwner, {
            it.let {
                when (it.getStatus()) {
                    HandleError.DataStatus.SUCCESS -> {
                        (requireActivity() as MainActivity?)!!.hideProgressBar()
                        initRecycler(it.getData())
                    }
                    HandleError.DataStatus.ERROR -> {
                        (requireActivity() as MainActivity?)!!.hideProgressBar()
                        Toast.makeText(requireContext(), it.getError(), Toast.LENGTH_SHORT).show()
                    }
                    HandleError.DataStatus.CONNECTIONERROR -> {
                        (requireActivity() as MainActivity?)!!.hideProgressBar()
                        Toast.makeText(
                            requireContext(), it.getConnectionError(), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    private fun initRecycler(data: ArrayList<ArticlesItem>?) {
        viewRef.recyclerViewNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            newsAdapter = NewsAdapter(data!!, onArticleCallback)
            adapter = newsAdapter
        }
    }

}