package com.example.passwordmanager.helpers

interface ItemTouchHelperListener {
    fun onItemMove(from_position: Int, to_position: Int): Boolean
    fun onItemSwipe(position: Int)
}