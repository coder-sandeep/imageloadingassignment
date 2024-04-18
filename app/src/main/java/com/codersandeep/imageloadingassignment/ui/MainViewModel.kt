package com.codersandeep.imageloadingassignment.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codersandeep.imageloadingassignment.models.ArticlesResponse
import com.codersandeep.imageloadingassignment.repository.MainRepository
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val repository = MainRepository(this)
    val articlesLiveData = MutableLiveData<Response<ArticlesResponse>>()

    fun getAllArticles() {
        repository.getAllArticles()
    }
}