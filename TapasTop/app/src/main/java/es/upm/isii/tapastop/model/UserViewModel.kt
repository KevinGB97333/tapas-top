package es.upm.isii.tapastop.model

import android.R.drawable
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.opengl.ETC1.decodeImage
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
import es.upm.isii.tapastop.network.UserResponse
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

enum class restApiStatus { LOADING, ERROR, DONE }

class UserViewModel : ViewModel(){


    private val _currentUser = MutableLiveData<User>()

    val currentUser : LiveData<User> = _currentUser

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

    private val _status = MutableLiveData<restApiStatus>()
    val status : LiveData<restApiStatus> = _status

    private val password = "randomPass"

    init{
        resetUser()
    }



    fun getUser(username : String, password : String){
        viewModelScope.launch {
            //Status
            _status.value = restApiStatus.LOADING
            try{
                val response = TapasTopApi.retrofitService.getLoginUser(username,password)
                when(response.code()){
                    200 ->{ _currentUser.value = response.body()!!.user
                            _userProfileImg.value = decodeImage(_currentUser.value?.profileImg)
                            Log.d("Main Menu get user","Correct User ${_currentUser.value}")
                            _status.value = restApiStatus.DONE
                    }

                    else -> {_status.value = restApiStatus.ERROR
                            Log.d("Login Errror","Code: ${response.code()}")
                    }
                }
            }catch(e : Exception) {
                _status.value = restApiStatus.ERROR
            }
        }
    }
    fun createUser(){
        viewModelScope.launch {
            _status.value = restApiStatus.LOADING
            try{
                 _currentUser.value?.profileImg = encodeImage(_userProfileImg.value)
                val newUser : UserResponse = _currentUser.value?.let { UserResponse(it) }!!
                val response = newUser?.let { TapasTopApi.retrofitService.createUser(it) }
                when(response?.code()){
                    200 -> _status.value = restApiStatus.DONE
                    else -> _status.value = restApiStatus.ERROR
                }
            }catch(e: Exception){
                _status.value = restApiStatus.ERROR
            }
        }
    }
    private fun encodeImage(imageBitmap: Bitmap?) : String{
        val baos = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
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

    fun setUsername(username: String){
        _currentUser.value?.username = username
    }
    fun setUserEmail(email : String){
        _currentUser.value?.email = email
    }
    fun setUserPassword(password: String){
        _currentUser.value?.password = password
    }
    fun setName(name : String){
        _currentUser.value?.name = name
    }
    fun setLastName(lastName : String){
        _currentUser.value?.surname = lastName
    }
    fun setGender(gender : String){
        _currentUser.value?.gender = gender
    }
    fun setCountry(country : String){
        _currentUser.value?.country = country
    }
    fun setLocation(location : String){
        _currentUser.value?.location = location
    }
    fun setDescription(description : String){
        _currentUser.value?.description = description
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