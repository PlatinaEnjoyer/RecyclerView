package com.example.recyclerview

import android.app.Application
import com.example.recyclerview.model.UserService

class App : Application() {
   val userService = UserService()
}