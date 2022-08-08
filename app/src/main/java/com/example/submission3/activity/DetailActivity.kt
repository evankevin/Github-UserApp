package com.example.submission3.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.submission3.R
import com.example.submission3.adapter.SectionsPagerAdapter
import com.example.submission3.databinding.ActivityDetailBinding
import com.example.submission3.response.UserDetailResponse
import com.example.submission3.response.UserResponse
import com.example.submission3.viewmodel.DetailViewModel
import com.example.submission3.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var mainViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = obtainViewModel(this@DetailActivity)

        mainViewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        mainViewModel.userdetail.observe(this) {
            setUserData(it)
        }

        mainViewModel.isLoading.observe(this) { showLoading(it, binding.progressBarDetail) }

        viewPager()
        addUser()

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    //fungsi binding dengan adapter untuk menampilkan data
    private fun setUserData(user : UserDetailResponse){
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.ivAvatar)
            binding.tvdUsername.text = user.login
            binding.tvdName.text = user.name
            tvdFollowersInput.text = user.followers.toString()
            tvdFollowingInput.text = user.following.toString()
            tvdCompanyInput.text = user.company
            tvdLocationInput.text = user.location
        }
    }

    //Buat nampilin loading
    private fun showLoading(isLoading: Boolean, view: View) {
        if (isLoading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }

    //Buat tablayout di fragment follower dan following
    private fun viewPager(){
        val user = intent.getParcelableExtra<UserResponse>(EXTRA_USER) as UserResponse
        val login = Bundle()
        val sectionsPagerAdapter = SectionsPagerAdapter(this, login)
        val viewPager: ViewPager2 = binding.viewPager

        user.login?.let { mainViewModel.findUserDetail(it) }
        login.putString(EXTRA_FRAG, user.login)

        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    //Buat add User jika toblol favoritenya di klik
    private fun addUser(){
        val id = intent.getIntExtra(EXTRA_ID,0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)
        val login = intent.getStringExtra(EXTRA_LOGIN)
        val url = intent.getStringExtra(EXTRA_URL)
        var switched = false
        //memeriksa tombol switch nya di klik atau tidak (dalam keadaan on atau off)
        CoroutineScope(Dispatchers.IO).launch {
            val count = mainViewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count > 0) {
                    binding.toggleButton5.isChecked = true
                    switched = true
                } else {
                    binding.toggleButton5.isChecked = false
                    switched = false
                }
            }
        }
        //Jika tombol dalam keadaan klik(on), data akan dimasukan ke database
        binding.toggleButton5.setOnClickListener{
            switched  = !switched
            if(switched ){
                mainViewModel.addUserFav(id,login,avatarUrl,url)
            }else{
                mainViewModel.removeFromFav(id)
            }
            binding.toggleButton5.isChecked = switched
        }
    }

    //fungsi untuk share
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu2,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val url = mainViewModel.getShared().value?.url
        when (item.itemId){
            R.id.nav_fav->{
                val objectIntent = Intent(Intent.ACTION_SEND)
                objectIntent.type = "text/plain"
                objectIntent.putExtra(Intent.EXTRA_SUBJECT, "Github app:")
                objectIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check this guy : $url")
                startActivity(Intent.createChooser(objectIntent, "Share to:"))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object{
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_URL = "extra_url"
        const val  EXTRA_FRAG = "extra_frag"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

}


