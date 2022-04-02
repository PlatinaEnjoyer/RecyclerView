package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.databinding.ActivityMainBinding
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserService
import com.example.recyclerview.model.UsersListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter

    private val userService: UserService
        get() = (applicationContext as App).userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersAdapter(object: UserActionListener{
            override fun onUserMove(user: User, moveBy: Int) {
                userService.moveUser(user, moveBy)
            }

            override fun onUserDelete(user: User) {
                userService.deleteUser(user)
            }

            override fun onUserDetails(user: User) {
                Toast.makeText(this@MainActivity, "User ${user.name}", Toast.LENGTH_LONG).show()
            }

        })
        //adapter.users = userService.getUsers()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

       userService.addListener(userListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        userService.removeListener(userListener)
    }

    private val userListener: UsersListener = {
        adapter.users = it
    }
}