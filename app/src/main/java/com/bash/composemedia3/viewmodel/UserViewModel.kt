package com.bash.composemedia3.viewmodel

import androidx.lifecycle.ViewModel
import com.bash.composemedia3.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<User>(User())

    val user = _user.asStateFlow()


    fun setUser(user: User) {
        _user.value = user
    }


}