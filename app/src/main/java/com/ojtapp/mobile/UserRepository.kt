package com.ojtapp.mobile

import kotlinx.coroutines.flow.Flow

interface UserRepository{
    val user: Flow<User>

    fun clearUser()
    fun updateUser(newUser: User)
}