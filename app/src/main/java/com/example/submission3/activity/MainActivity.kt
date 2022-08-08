package com.example.submission3.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.R
import com.example.submission3.SettingPreferences
import com.example.submission3.viewmodel.SettingViewModel
import com.example.submission3.adapter.SearchListAdapter
import com.example.submission3.databinding.ActivityMainBinding
import com.example.submission3.response.UserResponse
import com.example.submission3.viewmodel.MainViewModel
import com.example.submission3.viewmodel.SetViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private var listUser = ArrayList<UserResponse>()
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme()
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        mainViewModel.user.observe(this) { user ->
            if (user != null) {
                setUserData(user as ArrayList<UserResponse>)
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it, binding.progressBar)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        onSearching()


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.nav_fav->{

                val objectIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(objectIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //Fungsi untuk intent menuju detail activity
    private fun showSelectedUser(data: UserResponse) {
        Toast.makeText(this, "You choose " + data.login, Toast.LENGTH_SHORT).show()
        val objectIntent = Intent(this@MainActivity, DetailActivity::class.java)
        objectIntent.putExtra(DetailActivity.EXTRA_USER, data)
        objectIntent.putExtra(DetailActivity.EXTRA_ID, data.id)
        objectIntent.putExtra(DetailActivity.EXTRA_LOGIN, data.login)
        objectIntent.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
        objectIntent.putExtra(DetailActivity.EXTRA_URL, data.url)
        startActivity(objectIntent)
    }

    //Fungsi untuk searching
    private fun onSearching(){
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchBar.clearFocus()
                mainViewModel.findUser(query)
                binding.rvUser.visibility = View.VISIBLE
                setUserData(listUser)
                return true

            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    //Fungsi untuk mengambil data dan memasukannya ke adapter
    private fun setUserData(listUser: ArrayList<UserResponse>) {

        val listGithub = ArrayList<UserResponse>()
        for (user in listUser){
            listGithub.clear()
            listGithub.addAll(listUser)
        }
        val adapter = SearchListAdapter(listGithub)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : SearchListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserResponse) {
                showSelectedUser(data)

            }
        })
    }

    //Fungsi untuk ganti tema
    private fun setTheme(){
        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)
        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, SetViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }



    private fun showLoading(isLoading: Boolean, view: View) {
        if (isLoading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }

}

