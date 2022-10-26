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

    //Profile image of the current user
    private val _userProfileImg = MutableLiveData<Drawable>()
    val userProfileImg : LiveData<Drawable> = _userProfileImg
    private var profilePicIsSet = false

    //Username of the current user
    private val _username = MutableLiveData<String>()
    val username : LiveData<String> = _username

    //Name of the current user
    private val _name = MutableLiveData<String>()
    val name : LiveData<String> = _name

    //Email of the current user
    private val _userEmail = MutableLiveData<String>()
    val userEmail : LiveData<String> = _userEmail

    //Gender of the current user
    private val _gender = MutableLiveData<String>()
    val gender : LiveData<String> = _gender

    //Country of the current user
    private val _country = MutableLiveData<String>()
    val country : LiveData<String> = _country

    //Location of the current user
    private val _location = MutableLiveData<String>()
    val location : LiveData<String> = _location

    //Description of the current user
    private val _description = MutableLiveData<String>()
    val description : LiveData<String> = _description

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

    fun resetUser() {
        profilePicIsSet = false
        _username.value = ""
        _name.value = ""
        _userEmail.value = ""
        _gender.value = ""
        _country.value = ""
        _location.value = ""
        _description.value = ""
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
    fun setProfileImage(image : Drawable){
        _userProfileImg.value = image
        profilePicIsSet = true
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
     * Set the current name
     *
     * @param name is the current name
     */
    fun setName(name : String){
        _name.value = name
    }

    /**
     * Set the current user email
     * @param email is user email
     */
    fun setEmail(email : String){
        _userEmail.value = email
    }

    /**
     * Set the user gender
     *
     * @param gender of the user
     */
    fun setGender(gender : String){
        _gender.value = gender
    }

    /**
     * Set user country
     *
     * @param country of the user
     */
    fun setCountry(country : String){
        _country.value = country
    }

    /**
     * Set user location
     *
     * @param location of the user
     */
    fun setLocation(location : String){
        _location.value = location
    }

    /**
     * Set user description
     *
     * @param description of the user
     */
    fun setDescription(description : String){
        _description.value = description
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
        return _gender.value.isNullOrEmpty()
    }
    fun hasProfilePicSet() : Boolean{
        return profilePicIsSet
    }
}