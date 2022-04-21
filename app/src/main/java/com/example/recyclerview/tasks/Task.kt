package com.example.recyclerview.tasks

typealias Callback<T> = (T) -> Unit

interface Task<T> {

    fun onSuccess(callback: Callback<T>): Task<T>

    fun onError(callback: Callback<Throwable>): Task<T>

    fun cancel()

    fun await(): T
}