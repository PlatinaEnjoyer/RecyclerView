package com.example.recyclerview.tasks

sealed class MyResult<T> {
    fun <R> map(mapper: (T) -> R) :MyResult<R> {
        if(this is SuccessResult) return SuccessResult(mapper(data))
        return this as MyResult<R>
    }
}



class SuccessResult<T> (
    val data: T
    ) : MyResult<T>()

class ErrorResult<T>(
    val error: Throwable
): MyResult<T>()

class PendingResult<T> : MyResult<T>()

class EmptyResult<T> : MyResult<T>()
