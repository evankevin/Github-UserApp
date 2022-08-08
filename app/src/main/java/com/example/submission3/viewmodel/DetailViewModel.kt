package com.example.submission3.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.submission3.repository.UserFavRepository
import com.example.submission3.api.ApiConfig
import com.example.submission3.database.UserFav
import com.example.submission3.database.UserFavDao
import com.example.submission3.database.UserFavDatabase
import com.example.submission3.response.UserDetailResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _userdetail = MutableLiveData<UserDetailResponse>()
    val userdetail: LiveData<UserDetailResponse> = _userdetail

    private var userDao: UserFavDao
    private var userDb:UserFavDatabase

    private val mUserRepository: UserFavRepository = UserFavRepository(application)

    init {
        userDb = UserFavDatabase.getDatabase(application)
        userDao = userDb.UserFavDao()
    }


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "DetailViewModel"
    }


    fun findUserDetail(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetail(username)
        client.enqueue(object : Callback<UserDetailResponse>{
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ){
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userdetail.value = response.body()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }

            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }

    fun getShared(): LiveData<UserDetailResponse> = userdetail

    //fungsi untuk menambahkan user ke database
    fun addUserFav(id: Int, login: String?, avatarUrl: String?, url: String?){
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserFav(
                id,
                login,
                avatarUrl,
                url
            )
            mUserRepository.insert(user)
        }
    }

    //fungsi untuk meremove user dari database
    fun removeFromFav(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            mUserRepository.delete(id)
        }
    }
    //fungsi untuk ngecek id di database
    fun checkUser(id: Int)= userDao.getTotal(id)



}