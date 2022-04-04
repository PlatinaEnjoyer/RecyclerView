package com.example.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserService
import com.example.recyclerview.model.UsersListener

class UsersListViewModel(
    private val userService: UserService
) : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val listener: UsersListener = {
        _users.value = it
    }

    init {
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        userService.removeListener(listener)
    }

    fun loadUsers(){

    }

    fun deleteUser(user: User){
        userService.deleteUser(user)
    }

    fun moveUser(user: User, moveBy: Int){
        userService.moveUser(user, moveBy)
    }


}