package com.example.recyclerview.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recyclerview.UserActionListener
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserService
import com.example.recyclerview.model.UsersListener
import com.example.recyclerview.tasks.*

data class UserListItem(
    val user: User,
    val isInProgress: Boolean
)


class UsersListViewModel(
    private val userService: UserService
) : BaseViewModel(), UserActionListener {

    private val _users = MutableLiveData<MyResult<List<UserListItem>>>()
    val users: LiveData<MyResult<List<UserListItem>>> = _users

    private val _actionShowDetails = MutableLiveData<Event<User>>()
    val actionShowDetails: LiveData<Event<User>> = _actionShowDetails

    private val usersIdsInProgress = mutableListOf<Long>()
    private var usersResult: MyResult<List<User>> = EmptyResult()
        set(value){
            field = value
            notifyUpdates()
        }

    private val listener: UsersListener = {
        usersResult = if(it.isEmpty()){
            EmptyResult()
        } else {
            SuccessResult(it)
        }
    }

    init {
        userService.addListener(listener)
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        userService.removeListener(listener)
    }

    fun loadUsers(){
        usersResult = PendingResult()
        userService.loadUsers().onError {
            usersResult = ErrorResult(it)
        }
            .autoCancel()
    }

    override fun onUserDelete(user: User){
        if (isInProgress(user)) return
        addProgressTo(user)
        userService.deleteUser(user)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
            }
            .autoCancel()

    }

    override fun onUserMove(user: User, moveBy: Int){
        if (isInProgress(user)) return
        addProgressTo(user)
        userService.moveUser(user, moveBy)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
            }
            .autoCancel()
    }

    override fun onUserDetails(user: User) {
        _actionShowDetails.value = Event(user)
    }

    private fun addProgressTo(user: User) {
        usersIdsInProgress.add(user.id)
        notifyUpdates()
    }

    private fun removeProgressFrom(user: User){
        usersIdsInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User): Boolean{
        return usersIdsInProgress.contains(user.id)
    }


    private fun notifyUpdates() {
        _users.postValue(usersResult.map { users ->
            users.map { user -> UserListItem(user, isInProgress(user)) }
        })
    }
}