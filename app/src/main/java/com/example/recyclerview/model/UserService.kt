package com.example.recyclerview.model

import com.example.recyclerview.tasks.SimpleClass
import com.example.recyclerview.tasks.Task
import com.github.javafaker.Faker
import java.lang.Exception
import java.util.*
import java.util.concurrent.Callable
import kotlin.collections.ArrayList

typealias UsersListener = (users: List<User>) -> Unit  // что это делает (спросить про observer)

class UserService {
    private var users = mutableListOf<User>()
    private var loaded = false

    private val listeners = mutableListOf<UsersListener>()




    fun loadUsers(): Task<Unit> = SimpleClass<Unit>(Callable {
        Thread.sleep(2000)
        val faker = Faker.instance()
        users = (1..100).map { User(id = it.toLong(), name = faker.name().name(), company = faker.company().name(), photo = "https://picsum.photos/id/${it}/400") }.toMutableList()
        loaded = true
        notifyChanges()
    })

    fun getUserById(id: Long) : Task<UserDetails> = SimpleClass<UserDetails>(Callable {
        Thread.sleep(2000)
        val user = users.firstOrNull{it.id == id} ?: throw Exception()
        return@Callable UserDetails(
            user = user,
            details = Faker.instance().lorem().paragraphs(3).joinToString("\n\n")
        )
    })

    fun deleteUser(user: User): Task<Unit> = SimpleClass<Unit>(Callable {
        Thread.sleep(2000)
        val indexToDelete = users.indexOfFirst { it.id == user.id }
        if(indexToDelete != -1){
            users = ArrayList(users)
            users.removeAt(indexToDelete)
        }
        notifyChanges()
    })

    fun moveUser(user: User, moveBy: Int): Task<Unit> = SimpleClass<Unit>(Callable {
        Thread.sleep(2000)
        users = ArrayList(users)
        val oldIndex = users.indexOfFirst { it.id == user.id }
        if (oldIndex == -1) return@Callable
        val newIndex = oldIndex + moveBy
        if(newIndex < 0 || newIndex >= users.size) return@Callable
        Collections.swap(users, oldIndex, newIndex)
        notifyChanges()
    })

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
