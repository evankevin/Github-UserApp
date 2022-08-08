package com.example.submission3.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission3.activity.DetailActivity
import com.example.submission3.adapter.FollowerAdapter
import com.example.submission3.databinding.FragmentFollowerBinding
import com.example.submission3.response.UserResponse
import com.example.submission3.viewmodel.FollowerViewModel

class FollowerFragment : Fragment() {
    private lateinit var followerViewModel : FollowerViewModel

    private var _binding: FragmentFollowerBinding?=null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowerViewModel::class.java]
    }

    private fun setUserFollower(listFollower: List<UserResponse>) {

        val listGithub = ArrayList<UserResponse>()
        with(binding) {
            for (user in listFollower) {
                listGithub.clear()
                listGithub.addAll(listFollower)
            }
            val adapter = FollowerAdapter(listFollower)
            this?.rvFollower?.layoutManager = LinearLayoutManager(context)
            this?.rvFollower?.adapter = adapter

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followerViewModel.follower.observe(viewLifecycleOwner)
            {follower -> setUserFollower(follower)}

        followerViewModel.isLoading.observe(viewLifecycleOwner)
            { binding?.let { it1 -> showLoading(it, it1.progressBarFollower) } }

        followerViewModel.findFollower(arguments?.getString(DetailActivity.EXTRA_FRAG).toString())

    }
    private fun showLoading(isLoading: Boolean, view: View) {
        if (isLoading) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.INVISIBLE
        }
    }


}