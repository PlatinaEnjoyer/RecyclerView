package com.example.recyclerview.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.UserActionListener
import com.example.recyclerview.UsersAdapter
import com.example.recyclerview.databinding.FragmentUsersListBinding
import com.example.recyclerview.model.User
import com.example.recyclerview.tasks.EmptyResult
import com.example.recyclerview.tasks.ErrorResult
import com.example.recyclerview.tasks.PendingResult
import com.example.recyclerview.tasks.SuccessResult
import java.lang.Error

class UsersListFragment : Fragment(  ) {
    private lateinit var binding: FragmentUsersListBinding
    private lateinit var adapter: UsersAdapter

    private val viewModel: UsersListViewModel by viewModels{ factory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)
        adapter = UsersAdapter(viewModel)

        viewModel.users.observe(viewLifecycleOwner, Observer {
            when (it) {
                is SuccessResult -> {
                    binding.usersRecyclerView.visibility = View.VISIBLE
                    adapter.users = it.data
                }
                is ErrorResult -> {

                }
                is PendingResult -> {

                }
                is EmptyResult -> {

                }
            }
        })

        viewModel.actionShowDetails.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let { user -> navigator().showDetails(user) }
        })

        val layoutManager = LinearLayoutManager(requireContext())

        binding.usersRecyclerView.adapter = adapter
        binding.usersRecyclerView.layoutManager = layoutManager



        return binding.root
    }
}