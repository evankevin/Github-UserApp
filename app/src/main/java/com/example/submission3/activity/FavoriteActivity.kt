package com.example.submission3.activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.adapter.FavoriteAdapter
import com.example.submission3.database.UserFav
import com.example.submission3.database.UserFavDao
import com.example.submission3.database.UserFavDatabase
import com.example.submission3.databinding.ActivityFavoriteBinding
import com.example.submission3.response.UserResponse
import com.example.submission3.viewmodel.DetailViewModel
import com.example.submission3.viewmodel.FavViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var mainViewModel: FavViewModel
    private lateinit var adapter: FavoriteAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = FavoriteAdapter()

        mainViewModel = ViewModelProvider(this)[FavViewModel::class.java]


        mainViewModel.getUserFav().observe(this) {
            val list = getList(it)
            adapter.setListUser(list)
            if (list.isEmpty()){
                binding.noDataText.visibility = View.VISIBLE
            }else{
                binding.noDataText.visibility = View.INVISIBLE
            }

        }

        showSelectedUser()

    }


    //Fungsi untuk mengambil data dan memasukannya ke adapter
    private fun getList(users: List<UserFav>): ArrayList<UserResponse>{
        val listUser = ArrayList<UserResponse>()
        for (user in users){
            val userList = UserResponse(
                user.id,
                user.login,
                user.avatar_url,
                user.url
            )
            listUser.add(userList)
        }
       return listUser
    }

    //Fungsi untuk intent menuju detail activity
    private fun showSelectedUser(){
        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(user: UserResponse) {
                val objectIntent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                objectIntent.putExtra(DetailActivity.EXTRA_USER, user)
                objectIntent.putExtra(DetailActivity.EXTRA_ID, user.id)
                objectIntent.putExtra(DetailActivity.EXTRA_LOGIN, user.login)
                objectIntent.putExtra(DetailActivity.EXTRA_AVATAR, user.avatarUrl)
                objectIntent.putExtra(DetailActivity.EXTRA_URL, user.url)
                startActivity(objectIntent)

            }
        })
        binding.apply {
            rvUserFav.setHasFixedSize(true)
            rvUserFav.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUserFav.adapter=adapter
        }
    }

}
