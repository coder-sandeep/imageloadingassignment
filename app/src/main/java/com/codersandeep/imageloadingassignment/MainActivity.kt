package com.codersandeep.imageloadingassignment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codersandeep.imageloadingassignment.databinding.ActivityMainBinding
import com.codersandeep.imageloadingassignment.models.ArticlesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: RvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = RvAdapter(context = this)
        binding.mainRv.adapter = adapter

        if (isNetworkAvailable(this)) {
            binding.mainRv.visibility = View.VISIBLE
            binding.rlNoInternetView.visibility = View.GONE
            makeNetworkCall()
        } else {
            binding.mainRv.visibility = View.GONE
            binding.rlNoInternetView.visibility = View.VISIBLE
            binding.tvNoInternet1.text = "No Internet Connection"
            binding.tvNoInternet2.text =
                "No Internet found, Please check your connection or try again"
        }
        binding.btnTryAgain.setOnClickListener(this)
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val networkCapabilities =
                connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    private fun makeNetworkCall() {
        binding.pbLoading.visibility = View.VISIBLE
        binding.rlNoInternetView.visibility = View.GONE
        binding.mainRv.visibility = View.GONE
        CoroutineScope(Dispatchers.IO).launch {
            val call = ApiClient.apiService.getAllArticles(100)
            if (call.isSuccessful) {
                runOnUiThread {
                    binding.pbLoading.visibility = View.GONE
                    binding.rlNoInternetView.visibility = View.GONE
                    binding.mainRv.visibility = View.VISIBLE
                }
                call.body()?.let { setUpRv(it) }
            } else {
                runOnUiThread {
                    binding.rlNoInternetView.visibility = View.VISIBLE
                    binding.tvNoInternet1.text = "Something Went Wrong"
                    binding.tvNoInternet2.text =
                        "Something went wrong while connection to out server"
                    binding.mainRv.visibility = View.GONE
                    binding.pbLoading.visibility = View.GONE
                }
                Toast.makeText(baseContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpRv(articlesResponse: ArticlesResponse) {
        adapter.updateData(articlesResponse)

        runOnUiThread {
            adapter.notifyDataSetChanged()
        }

    }

    override fun onClick(v: View?) {
        if (v?.id == binding.btnTryAgain.id) {
            if (isNetworkAvailable(context = baseContext))
                makeNetworkCall()
            else
                Toast.makeText(
                    baseContext,
                    "Please check you internet connection",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

}


