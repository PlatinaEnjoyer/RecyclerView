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
import com.example.recyclerview.tasks.SuccessResult

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

        viewModel.actionGoBack.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let { navigator().goBack() }
        })

        viewModel.actionShowToast.observe(viewLifecycleOwner, Observer {
            it.getValue()?.let { messageRes -> navigator().toast(messageRes) }
        })

        viewModel.state.observe(viewLifecycleOwner, Observer {
            binding.contentContainer.visibility = if (it.showContent) {
                val userDetails = (it.userDetailsResult as SuccessResult).data
                binding.textView.text = userDetails.user.name
                    Glide.with(this)
                        .load(userDetails.user.photo)
                        .circleCrop()
                        .into(binding.imageView)
                binding.textView2.text = userDetails.details

                View.VISIBLE
            } else {
                View.GONE
            }

            binding.progressbar.visibility = if (it.showProgress) View.VISIBLE else View.GONE
            binding.button.isEnabled = it.enableDeleteButton
        })


        binding.button.setOnClickListener {
            viewModel.deleteUser()
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