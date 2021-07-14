package ru.netology.nmedia.data

import ru.netology.nmedia.api.Api
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.data.model.LoggedInUser
import ru.netology.nmedia.error.ApiError

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): AuthState {
        // handle login
        val response = Api.retrofitService.updateUser(username, password)
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }

        val body = response.body() ?: throw ApiError(response.code(), response.message())
        AppAuth.getInstance().setAuth(body.id, body.token)
        return body
//        val result = dataSource.login(username, password)
//
//        if (result is Result.Success) {
//            setLoggedInUser(result.data)
//        }
//
//        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}