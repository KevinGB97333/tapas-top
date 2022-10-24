package es.upm.isii.tapastop.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel(){

    //Username of the current user
    private val _username = MutableLiveData<String>()
    val username : LiveData<String> = _username

    //Email of the current user
    private val _userEmail = MutableLiveData<String>()
    val userEmail : LiveData<String> = _userEmail

    //Password of the current user
    private val _userPassword = MutableLiveData<String>()
    val userPassword : LiveData<String> = _userPassword

    init{
        resetUser()
    }
    fun resetUser() {

    }
}