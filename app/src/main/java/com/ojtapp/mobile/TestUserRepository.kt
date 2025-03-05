package com.ojtapp.mobile

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestUserRepository: UserRepository {
    private val _user = MutableStateFlow(User())
    override val user = _user.asStateFlow()

    override fun updateUser(newUser: User) {
        _user.update { newUser }
    }
}