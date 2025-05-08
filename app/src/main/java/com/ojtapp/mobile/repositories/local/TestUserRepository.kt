package com.ojtapp.mobile.repositories.local

import android.util.Log
import com.ojtapp.mobile.repositories.UserRepository
import com.ojtapp.mobile.model.User
import com.ojtapp.mobile.model.UserPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestUserRepository(
    private val userPreference: UserPreference
): UserRepository {

    private val _user = MutableStateFlow(User())
    override val user = _user.asStateFlow()

    override fun clearUser() {
        userPreference.clearUser()
        _user.update { User() }
    }

    override fun updateUser(newUser: User) {
        Log.d("UserRep", newUser.token)
        userPreference.user = newUser
        _user.update { newUser }
    }

}