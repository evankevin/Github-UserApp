package com.example.submission3.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.activity.DetailActivity
import com.example.submission3.adapter.FollowingAdapter
import com.example.submission3.databinding.FragmentFollowingBinding
import com.example.submission3.response.UserResponse
import com.example.submission3.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var followingViewModel : FollowingViewModel
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowingViewModel::class.java]
    }

    private fun setUserFollowing(listFollowing: List<UserResponse>) {

        val listGithub = ArrayList<UserResponse>()
        with(binding) {
            for (user in listFollowing) {
                listGithub.clear()
                listGithub.addAll(listFollowing)
            }
            val adapter = FollowingAdapter(listFollowing)
            this?.rvFollowing?.layoutManager = LinearLayoutManager(context)
            this?.rvFollowing?.adapter = adapter

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followingViewModel.following.observe(viewLifecycleOwner)
            {following -> setUserFollowing(following)}

        followingViewModel.isLoading.observe(viewLifecycleOwner)
            { binding?.let { it1 -> showLoading(it, it1.progressBarFollowing) } }

        followingViewModel.findFollowing(arguments?.getString(DetailActivity.EXTRA_FRAG).toString())

    }
    private fun showLoading(isLoading: Boolean, view: View) {
        if (isLoading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }

}