package com.ojtapp.mobile.repositories

import com.ojtapp.mobile.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository{
    val user: Flow<User>

    fun clearUser()
    fun updateUser(newUser: User)
}