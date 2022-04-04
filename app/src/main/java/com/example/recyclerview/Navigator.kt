package com.example.recyclerview

import com.example.recyclerview.model.User

interface Navigator {
    fun showDetails(user: User){
    }
    fun goBack(){

    }

    fun toast(massageRes: Int){

    }
}