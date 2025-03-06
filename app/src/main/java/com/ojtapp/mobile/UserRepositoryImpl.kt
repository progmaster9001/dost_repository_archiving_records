package com.ojtapp.mobile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserRepositoryImpl(
    private val userPreference: UserPreference
) : UserRepository {

    private val _user = MutableStateFlow(userPreference.user)
    override val user = _user.asStateFlow()

    override fun clearUser() {
        userPreference.clearUser()
    }

    override fun updateUser(newUser: User) {
        _user.update { newUser }
        userPreference.user = newUser
    }
}