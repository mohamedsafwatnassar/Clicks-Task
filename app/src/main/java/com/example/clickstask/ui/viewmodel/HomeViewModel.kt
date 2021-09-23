package com.example.clickstask.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.clickstask.BuildConfig
import com.example.clickstask.apis.ApiManager
import com.example.clickstask.handleData.ErrorLiveData
import com.example.clickstask.ui.model.ArticlesItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val apiManager = ApiManager().apis

    var handleData = ErrorLiveData<ArrayList<ArticlesItem>>()

    var selectedNews = ArticlesItem()

    fun getNewsSource() {
        val handler = CoroutineExceptionHandler { _, exception ->
            handleData.postConnectionError(exception.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO).launch(handler) {
            try {
                val response = apiManager.getNewsSource("eg", BuildConfig.apiKey)
                if (response.isSuccessful) {
                    handleData.postSuccess(response.body()!!.articles)

                } else {
                    handleData.postError("Something went wrong")
                }
            } catch (e: Exception) {
                handleData.postConnectionError("Internet Connection Failure")
            }
        }
    }

}