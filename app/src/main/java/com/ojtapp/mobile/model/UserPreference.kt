package com.ojtapp.mobile.model

import android.content.SharedPreferences
import com.ojtapp.mobile.data.RepositoryMode
import kotlinx.serialization.json.Json

class UserPreference(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_USER = "key_user"
        private const val REPOSITORY_MODE = "repo_mode"
    }

    private val json = Json { ignoreUnknownKeys = true }

    var user: User
        get() {
            val userJson = sharedPreferences.getString(KEY_USER, null) ?: return User()
            return json.decodeFromString(userJson)
        }
        set(value) {
            sharedPreferences.edit()
                .putString(KEY_USER, json.encodeToString(value))
                .apply()
        }

    var repositoryMode: RepositoryMode
        get() {
            val repMode = sharedPreferences.getString(REPOSITORY_MODE, null) ?: return RepositoryMode.REMOTE
            return json.decodeFromString(repMode)
        }
        set(value) {
            sharedPreferences.edit()
                .putString(REPOSITORY_MODE, json.encodeToString(value))
                .apply()
        }

    fun clearUser() {
        sharedPreferences.edit().remove(KEY_USER).apply()
    }
}