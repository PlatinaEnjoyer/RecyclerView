package com.example.recyclerview.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recyclerview.App
import java.lang.IllegalStateException

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass){
            UsersListViewModel::class.java -> {
                UsersListViewModel(app.userService)
            }
            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}


fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)