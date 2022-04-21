package com.example.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview.R
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserDetails
import com.example.recyclerview.model.UserService
import com.example.recyclerview.tasks.EmptyResult
import com.example.recyclerview.tasks.MyResult
import com.example.recyclerview.tasks.PendingResult
import com.example.recyclerview.tasks.SuccessResult
import com.github.javafaker.Bool
import java.lang.Exception

class UsersDetailViewModel(private val userService: UserService):BaseViewModel(){
    private val _state = MutableLiveData<State>()
    val state = _state

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast

    private val _actionGoBack = MutableLiveData<Event<Unit>>()
    val actionGoBack: LiveData<Event<Unit>> = _actionGoBack

    private val currentState: State get() = state.value!!

    init {
        _state.value = State(
            userDetailsResult = EmptyResult(),
            deletingInProgress = false
        )
    }

    fun loadUser(userId: Long){
        if (currentState.userDetailsResult is SuccessResult) return
        _state.value = currentState.copy(userDetailsResult = PendingResult())

        userService.getUserById(userId)
            .onSuccess {
                _state.value = currentState.copy(userDetailsResult = SuccessResult(it))
            }
            .onError {
                _actionShowToast.value = Event(R.string.cant_load_user_details)
                _actionGoBack.value = Event(Unit)
            }
            .autoCancel()
    }

    fun deleteUser(){
        val userDetailsResult = currentState.userDetailsResult
        if(userDetailsResult !is SuccessResult) return
        _state.value = currentState.copy(deletingInProgress = true)
        userService.deleteUser(userDetailsResult.data.user)
            .onSuccess {
                _actionShowToast.value = Event(R.string.user_deleted)
                _actionGoBack.value = Event(Unit)
            }
            .onError {
                _state.value = currentState.copy(deletingInProgress = false)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }

    data class State(
        val userDetailsResult: MyResult<UserDetails>,
        private val deletingInProgress: Boolean
    ){
        val showContent: Boolean get() = userDetailsResult is SuccessResult
        val showProgress: Boolean get() = userDetailsResult is PendingResult || deletingInProgress
        val enableDeleteButton: Boolean get() = !deletingInProgress
    }
}