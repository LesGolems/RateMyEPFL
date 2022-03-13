package com.github.sdp.ratemyepfl.placeholder

abstract class Repository<T> (private val dataSource : Database<T>){

    abstract fun add(value : T)

    abstract fun remove(value : T)
}