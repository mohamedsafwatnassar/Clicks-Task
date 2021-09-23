package com.example.clickstask.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.clickstask.databinding.FragmentDetailsBinding
import com.example.clickstask.ui.model.ArticlesItem
import com.example.clickstask.ui.viewmodel.HomeViewModel

class DetailsFragment : Fragment() {

    private lateinit var viewRef: FragmentDetailsBinding

    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var item: ArticlesItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        item = homeViewModel.selectedNews
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewRef = FragmentDetailsBinding.inflate(inflater, container, false)

        return viewRef.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeDate()

        setBtnListener()
    }

    private fun subscribeDate() {
        viewRef.txtSourceName.text = item.source!!.name.toString()
        viewRef.txtArticleTitle.text = item.title.toString()

        Glide.with(requireContext())
            .load(item.urlToImage)
            .into(viewRef.imgArticleNews)

        viewRef.txtArticleDesc.text = item.description.toString()
    }

    private fun shareNews() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(
            Intent.EXTRA_TEXT, (item.url)
        )
        intent.type = "text/plain"
        requireContext().startActivity(Intent.createChooser(intent, "Send To"))
    }

    private fun setBtnListener() {
        viewRef.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewRef.imgShare.setOnClickListener {
            shareNews()
        }
    }

}