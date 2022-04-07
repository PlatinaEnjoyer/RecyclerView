package com.example.recyclerview.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.recyclerview.databinding.FragmentUsersDetailBinding
import com.example.recyclerview.databinding.FragmentUsersListBinding

class UsersDetailFragment :Fragment() {
    private lateinit var binding: FragmentUsersDetailBinding
    private val viewModel: UsersDetailViewModel by viewModels{factory()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadUser(requireArguments().getLong("ARG_USER_ID"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersDetailBinding.inflate(layoutInflater, container, false)

        viewModel.usersDetails.observe(viewLifecycleOwner, Observer {
            binding.textView.text = it.user.name
            Glide.with(this).load(it.user.photo).circleCrop().into(binding.imageView)
            binding.textView2.text = it.details
        })

        binding.button.setOnClickListener {
            viewModel.deleteUser()
            navigator().goBack()
        }
        return binding.root
    }


    companion object{
        fun newInstance(userId: Long): UsersDetailFragment {
            val fragment = UsersDetailFragment(
            )
            fragment.arguments = bundleOf("ARG_USER_ID" to userId)
            return fragment
        }
    }
}