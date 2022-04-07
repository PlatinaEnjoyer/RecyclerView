package com.example.recyclerview.model

import com.github.javafaker.Faker
import java.util.*

typealias UsersListener = (users: List<User>) -> Unit  // что это делает (спросить про observer)

class UserService {
    private var users = mutableListOf<User>()

    private val listeners = mutableListOf<UsersListener>()


    init {
        val faker = Faker.instance()
        users = (1..100).map { User(id = it.toLong(), name = faker.name().name(), company = faker.company().name(), photo = "https://picsum.photos/id/${it}/400") }.toMutableList()
    }

    fun getUsers(): List<User>{
        return users
    }

    fun getUserById(id: Long) : UserDetails {
        val user = users.firstOrNull{it.id == id}
        return UserDetails(
            user = user!!,
            details = Faker.instance().lorem().paragraphs(3).joinToString("\n\n")
        )
    }

    fun deleteUser(user: User){
        val indexToDelete = users.indexOfFirst { it.id == user.id }
        if(indexToDelete != -1){
            users.removeAt(indexToDelete)
        }
        notifyChanges()
    }

    fun moveUser(user: User, moveBy: Int){
        val oldIndex = users.indexOfFirst { it.id == user.id }
        if (oldIndex == -1) return
        val newIndex = oldIndex + moveBy
        if(newIndex < 0 || newIndex >= users.size) return
        Collections.swap(users, oldIndex, newIndex)
        notifyChanges()
    }

    fun addListener(listener: UsersListener){
        listeners.add(listener)
        listener.invoke(users)
    }

    fun removeListener(listener: UsersListener){
        listeners.remove(listener)
    }

    private fun notifyChanges(){
        listeners.forEach{it.invoke(users)}  // что вот это делает и что вообще за листенеры
    }
}
