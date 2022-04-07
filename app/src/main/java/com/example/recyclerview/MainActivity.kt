package com.example.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recyclerview.databinding.ActivityMainBinding
import com.example.recyclerview.model.User
import com.example.recyclerview.model.UserService
import com.example.recyclerview.model.UsersListener
import com.example.recyclerview.screens.UsersDetailFragment
import com.example.recyclerview.screens.UsersListFragment

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container_main, UsersListFragment()).commit()
        }
    }

    override fun showDetails(user: User) {
        supportFragmentManager.beginTransaction().addToBackStack(null).replace(R.id.fragment_container_main, UsersDetailFragment.newInstance(user.id)).commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun toast(massageRes: Int) {
       Toast.makeText(this, massageRes, Toast.LENGTH_SHORT)
    }
}