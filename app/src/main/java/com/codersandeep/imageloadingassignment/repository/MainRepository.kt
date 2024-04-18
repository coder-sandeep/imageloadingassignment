package com.codersandeep.imageloadingassignment.repository

import android.util.Log
import com.codersandeep.imageloadingassignment.network.ApiClient
import com.codersandeep.imageloadingassignment.ui.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainRepository(var vm: MainViewModel) {

    private val api = ApiClient.apiService

    fun getAllArticles() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getAllArticles(100) //
                vm.articlesLiveData.postValue(response)
            } catch (e: Exception) {
                Log.e("Network log", "Something went wrong")
            }
        }
    }

}