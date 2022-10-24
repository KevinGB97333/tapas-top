package es.upm.isii.tapastop.model

import android.R.drawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Patterns
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.upm.isii.tapastop.R

class UserViewModel : ViewModel(){

    //Username of the current user
    private val _username = MutableLiveData<String>()
    val username : LiveData<String> = _username

    //Email of the current user
    private val _userEmail = MutableLiveData<String>()
    val userEmail : LiveData<String> = _userEmail

    //Email of the current user
    private val _userProfileImg = MutableLiveData<Drawable>()
    val userProfileImg : LiveData<Drawable> = _userProfileImg

    private val password = "randomPass"

    init{
        resetUser()
    }

    fun resetUser() {
        _username.value = ""
        _userEmail.value = ""
    }

    /**
     * Set the current username
     *
     * @param username is the current username
     */
    fun setUsername(username : String){
        _username.value = username
    }

    /**
     * Set the current user email
     * @param email is user email
     */
    fun setEmail(email : String){
        _userEmail.value = email
    }
}