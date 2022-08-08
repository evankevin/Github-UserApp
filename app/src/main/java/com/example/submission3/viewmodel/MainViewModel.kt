package com.example.submission3.viewmodel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission3.api.ApiConfig
import com.example.submission3.databinding.ActivityMainBinding
import com.example.submission3.response.SearchingResponse
import com.example.submission3.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {


    private val _user = MutableLiveData<List<UserResponse>?>()
    val user: MutableLiveData<List<UserResponse>?> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    companion object{
        private const val TAG = "MainViewModel"
    }


    fun findUser(query: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(query)
        client.enqueue(object : Callback<SearchingResponse>{
            override fun onResponse(
                call: Call<SearchingResponse>,
                response: Response<SearchingResponse>
            ){
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _user.value = response.body()?.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }

            }
            override fun onFailure(call: Call<SearchingResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }
}