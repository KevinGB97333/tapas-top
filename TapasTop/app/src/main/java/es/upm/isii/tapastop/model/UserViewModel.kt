package es.upm.isii.tapastop.model

import android.R.drawable
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.util.Base64
import android.util.Log
import android.util.Patterns
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.upm.isii.tapastop.R
import es.upm.isii.tapastop.network.TapasTopApi
import es.upm.isii.tapastop.network.User
import kotlinx.coroutines.launch

class UserViewModel : ViewModel(){


    private val _currentUser = MutableLiveData<User>()

    var currentUser : LiveData<User> = _currentUser

    //Profile image of the current user
    private val _userProfileImg = MutableLiveData<Bitmap>()
    val userProfileImg : LiveData<Bitmap> = _userProfileImg
    private var profilePicIsSet = false

    private val _degustationsAdded = MutableLiveData<Int>()
    val degustationAdded : LiveData<Int> = _degustationsAdded
    //Number of locals added by the user
    private val _localsAdded = MutableLiveData<Int>()
    val localsAdded : LiveData<Int> = _localsAdded

    //Number of friend request of the current user
    private val _friendRequests = MutableLiveData<Int>()
    val friendRequests : LiveData<Int> = _friendRequests

    //Number of awards of the current user
    private val _awards = MutableLiveData<Int>()
    val awards : LiveData<Int> = _awards

    private val password = "randomPass"

    init{
        resetUser()
    }



    fun getUser(username : String, password : String){
        viewModelScope.launch {
            //Status
            try{
                _currentUser.value = TapasTopApi.retrofitService.getLoginUser(username, password).user
                _userProfileImg.value = decodeImage(_currentUser.value?.profileImg)
                Log.d("ViewModel get user","Correct User ${_currentUser.value}")
            }catch(e : Exception){
                Log.d("ViewModel get user","${e.toString()}")
                Log.d("ViewModel get user", "ERROR")
                Log.d("ViewModel get user","User ${_currentUser.value}")
            }
        }
    }
    private fun decodeImage(imageB64 : String?) : Bitmap{
        val imageBytes = Base64.decode(imageB64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.size)
    }
    /**
     * Resets values to default when the viewModel initializes
     */
    private fun resetUser() {
        profilePicIsSet = false
        _currentUser.value = User("","","","","","","","","","")
        _degustationsAdded.value = 0
        _localsAdded.value = 0
        _friendRequests.value = 0
        _awards.value = 0
    }

    /**
     * Set user profile image
     *
     * @param image profile image
     */
    fun setProfileImage(image : Bitmap){
        _userProfileImg.value = image
        profilePicIsSet = true
    }

    /**
     * Set number of degustations added by the user
     *
     * @param degustationsNumber added by the user
     */
    fun setDegustationsNumber(degustationsNumber : Int){
        _degustationsAdded.value = degustationsNumber
    }

    /**
     * Set number of locals added by the user
     *
     * @param localsNumber added by the user
     */
    fun setLocalsNumber(localsNumber : Int){
        _localsAdded.value = localsNumber
    }

    /**
     * Set number of pending friend request of the user
     *
     * @param friendRequestNumber of the user
     */
    fun setFriendRequestNumber(friendRequestNumber : Int){
        _friendRequests.value = friendRequestNumber
    }

    /**
     *
     * Set number of award acquired by the user
     *
     * @param awardsNumber of the user
     */
    fun setAwardsNumber(awardsNumber : Int){
        _awards.value = awardsNumber
    }

    fun hasGenderSet() : Boolean{
        return _currentUser.value?.gender.isNullOrEmpty()
    }
    fun hasProfilePicSet() : Boolean{
        return profilePicIsSet
    }
}