package com.example.submission3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.submission3.repository.UserFavRepository
import com.example.submission3.database.UserFav

class FavViewModel(application: Application) : AndroidViewModel(application) {

    private val mUserRepository: UserFavRepository = UserFavRepository(application)
    //fungsi ngambil user yang ada di database
    fun getUserFav(): LiveData<List<UserFav>> = mUserRepository.getAllUser()

}