package com.example.recyclerview.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserDetails
import com.example.recyclerview.model.UserService

class UsersDetailViewModel(private val userService: UserService):ViewModel(){
    private val _usersDetails = MutableLiveData<UserDetails>()
    val usersDetails = _usersDetails

    fun loadUser(userId: Long){
        _usersDetails.value = userService.getUserById(userId)
    }

    fun deleteUser(){
        val userDetails = this.usersDetails.value ?: return
        userService.deleteUser(userDetails.user)
    }
}