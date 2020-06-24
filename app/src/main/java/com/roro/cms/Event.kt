package com.roro.cms

class Event<out T> (private val content : T){
    private var isDataDelivered : Boolean = false
        private set

    fun getData() : T?{
        return if(isDataDelivered)
                null
        else{
            isDataDelivered = true
            content
        }
    }
}