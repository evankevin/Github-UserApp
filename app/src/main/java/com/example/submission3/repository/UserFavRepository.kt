package com.example.submission3.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.submission3.database.UserFav
import com.example.submission3.database.UserFavDao
import com.example.submission3.database.UserFavDatabase

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserFavRepository(application: Application) {
    private val mUserDao: UserFavDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = UserFavDatabase.getDatabase(application)
        mUserDao = db.UserFavDao()
    }
    fun getAllUser(): LiveData<List<UserFav>> = mUserDao.getUserFav()
    fun insert(userFav: UserFav) {
        executorService.execute { mUserDao.insert(userFav) }
    }
    fun delete(id : Int) {
        executorService.execute { mUserDao.delete(id) }
    }

    fun getTotal(id: Int){
        executorService.execute { mUserDao.getTotal(id) }
    }
}