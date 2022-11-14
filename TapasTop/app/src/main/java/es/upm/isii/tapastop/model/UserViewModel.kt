package es.upm.isii.tapastop.model

import android.accounts.NetworkErrorException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.ETC1.decodeImage
import android.opengl.ETC1.encodeImage

import android.util.Base64
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.upm.isii.tapastop.network.*
import kotlinx.coroutines.*
import retrofit2.Response
import java.io.ByteArrayOutputStream

enum class restApiStatus { NOTHING, LOADING, ERROR, DONE }

enum class usernameAvailability { NOTHING, LOADING, EXIST, AVAILABLE, ERROR }

enum class userGetApiStatus { NOTHING, LOADING, DONE}
class UserViewModel : ViewModel() {


	private val _currentUser = MutableLiveData<User>()

	val currentUser: LiveData<User> = _currentUser

	//Profile image of the current user
	private val _userProfileImg = MutableLiveData<Bitmap>()
	val userProfileImg: LiveData<Bitmap> = _userProfileImg
	private var profilePicIsSet = false

	private val _degustationsAdded = MutableLiveData<Int>()
	val degustationAdded: LiveData<Int> = _degustationsAdded

	//Number of locals added by the user
	private val _localsAdded = MutableLiveData<Int>()
	val localsAdded: LiveData<Int> = _localsAdded

	//Friend requests of the current user
	private val _friendRequests = MutableLiveData<Users>()
	val friendRequests: LiveData<Users> = _friendRequests

	//Friends of the current user
	private val _friends = MutableLiveData<Users>()
	val friends : LiveData<Users> = _friends

	//Number of awards of the current user
	private val _awards = MutableLiveData<Int>()
	val awards: LiveData<Int> = _awards

	private val _status = MutableLiveData<restApiStatus>()
	val status: LiveData<restApiStatus> = _status

	private val _userGetStatus = MutableLiveData<userGetApiStatus>()
	val userGetStatus: LiveData<userGetApiStatus> = _userGetStatus

	private val _checkUsernameStatus = MutableLiveData<usernameAvailability>()
	val checkUsernameStatus: LiveData<usernameAvailability> = _checkUsernameStatus

	lateinit var userBackup: User

	private val _users = MutableLiveData<Users>()
	val users : LiveData<Users> =_users

	private val _tempUser = MutableLiveData<User>()
	val tempUser : LiveData<User> = _tempUser

	private val _tempUserImg = MutableLiveData<Bitmap>()
	val tempUserImg: LiveData<Bitmap> = _tempUserImg

	var requested : MutableList<String> = mutableListOf()
	var code: String = ""
	var usernameRecovery : String = ""
	init {
		resetUser()
	}
	fun resetStatus(){
		_checkUsernameStatus.value = usernameAvailability.NOTHING
		_status.value = restApiStatus.NOTHING
		_userGetStatus.value = userGetApiStatus.NOTHING
	}
	fun resetUsersList(){
		_users.value = Users(mutableListOf())
	}
	/**
	 * Resets values to default when the viewModel initializes
	 */
	fun resetUser() {
		code = ""
		usernameRecovery = ""
		resetStatus()
		requested = mutableListOf()
		_users.value = Users(mutableListOf())
		profilePicIsSet = false
		_tempUser.value =User("", "", "", "", "", "", "", "", "", "")
		_currentUser.value = User("", "", "", "", "", "", "", "", "", "")
		_degustationsAdded.value = 0
		_localsAdded.value = 0
		_friendRequests.value = Users(mutableListOf())
		_friends.value = Users(mutableListOf())
		_awards.value = 0
	}
	fun sendFriendRequest(){
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try{
				val response = TapasTopApi.retrofitService.sendFriendRequest(_tempUser.value!!.username, _currentUser.value!!.username)
				when(response.code()){
					200 -> {
						requested.add(requested.size,_tempUser.value!!.username)
						_status.value = restApiStatus.DONE
					}
					else ->  {
						_status.value = restApiStatus.ERROR
					}
				}
			}catch (e : Exception){
				_status.value = restApiStatus.ERROR
			}
		}
	}
	fun getSpecificUser(username: String){
		viewModelScope.launch {
			_userGetStatus.value = userGetApiStatus.LOADING
			try{
				val response = TapasTopApi.retrofitService.getUser(username)
				when(response.code()){
					200 -> {
						_tempUser.value = response.body()!!.user
						_tempUserImg.value = decodeImage(_tempUser.value?.profileImg)
						_userGetStatus.value = userGetApiStatus.DONE
					}
					else -> {_userGetStatus.value = userGetApiStatus.NOTHING}
				}
			}catch (e : Exception){
				_userGetStatus.value = userGetApiStatus.NOTHING
			}
		}
	}
	fun getUsers(searchString : String){
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try{
				val response = TapasTopApi.retrofitService.getUsersLike(searchString, _currentUser.value!!.username.toString())
				when(response.code()){
					200 -> {
						_users.value = response.body()
						_status.value = restApiStatus.DONE
					}
					else ->{
						_status.value = restApiStatus.ERROR
						_users.value = Users(mutableListOf())
					}
				}
			}catch (e : Exception){
				_status.value = restApiStatus.ERROR
				_users.value = Users(mutableListOf())
			}
		}
	}
	fun updatePassword(newPassword : String){
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try{
				val response = TapasTopApi.retrofitService.changePassword(usernameRecovery, newPassword)
				when(response.code()){
					200 -> _status.value = restApiStatus.DONE
					else -> _status.value = restApiStatus.ERROR
				}
			}catch (e : Exception){
				_status.value = restApiStatus.ERROR
			}
		}
	}
	fun sendRecoveryEmail(username: String){
		generateCode()
		usernameRecovery = username
		viewModelScope.launch {
			try{
				TapasTopApi.retrofitService.recoverPassword(username,code)
			}catch (e : Exception){
				Log.d("Send email recovery exception", "${e.printStackTrace()}")
			}
		}
	}
	fun updateUser(){
		_status.value = restApiStatus.LOADING
		try{
			viewModelScope.launch {
				_currentUser.value?.profileImg = encodeImage(_userProfileImg.value)
				val updatedUser: UserResponse = _currentUser.value?.let { UserResponse(it) }!!
				val response = TapasTopApi.retrofitService.updateUser(userBackup.username,updatedUser)
				when(response.code()){
					200 -> {
						_status.value = restApiStatus.DONE
					}
					else -> {
						_currentUser.value = userBackup
						_status.value = restApiStatus.ERROR
					}
				}
			}
		}catch (e: Exception){
			_currentUser.value = userBackup
			_status.value = restApiStatus.ERROR
		}
	}
	fun checkUsername(username: String) {
		_checkUsernameStatus.value = usernameAvailability.LOADING
		try {
			viewModelScope.launch {
				val response = TapasTopApi.retrofitService.checkUsernameAvailability(username)
				when (response.code()) {
					200 -> {
						_checkUsernameStatus.value = usernameAvailability.EXIST
					}

					404 -> {
						_checkUsernameStatus.value = usernameAvailability.AVAILABLE
					}

					else -> {
						_checkUsernameStatus.value = usernameAvailability.ERROR
					}
				}
			}
		}catch(e : Exception){
            _checkUsernameStatus.value = usernameAvailability.ERROR
        }
	}

	/**
	 * Retrieve user from the server
	 *
	 * @param username of the desired user
	 * @param password of the desired user
	 */
	fun getUser(username: String, password: String) {
		viewModelScope.launch {
			//Status
			_status.value = restApiStatus.LOADING
			try {
				val response = TapasTopApi.retrofitService.getLoginUser(username, password)
				when (response.code()) {
					200 -> {
						_currentUser.value = response.body()!!.user
						_userProfileImg.value = decodeImage(_currentUser.value?.profileImg)
						var responseFriends : Response<Users>

						val jobFriendRequests = launch {
							responseFriends = TapasTopApi.retrofitService.getFriendRequests(username)
							when(responseFriends.code()){
								200 -> _friendRequests.value = responseFriends.body()
								400 -> _status.value = restApiStatus.ERROR
							}
						}
						jobFriendRequests.join()
						if(_status.value == restApiStatus.ERROR){
							throw NetworkErrorException()
						}
						val jobFriends = launch {
							responseFriends = TapasTopApi.retrofitService.getFriends(username)
							when(responseFriends.code()){
								200 -> _friends.value = responseFriends.body()
								400 -> _status.value = restApiStatus.ERROR
							}
						}
						jobFriends.join()
						if(_status.value == restApiStatus.ERROR){
							throw NetworkErrorException()
						}
						_status.value = restApiStatus.DONE

					}
					else -> {
						_status.value = restApiStatus.ERROR
					}
				}
			} catch (e: Exception) {
				_friends.value = Users(mutableListOf())
				_friendRequests.value = Users(mutableListOf())
				_status.value = restApiStatus.ERROR
			}
		}
	}
	fun userInFriends() : Boolean{
		var user : UserSummary ?=null
		user = _friends.value?.users?.find {
			it.username == _tempUser.value?.username
		}
		if(user == null){
			return false
		}
		return user?.username!=""
	}
	fun getFriendRequestsUpdate(){
		viewModelScope.launch {
			try{
				val responseFriends = TapasTopApi.retrofitService.getFriendRequests(_currentUser.value!!.username)
				when(responseFriends.code()){
					200 -> _friendRequests.value = responseFriends.body()
					else ->{}
				}
			}catch (e : Exception){
			}
		}
	}
	fun getFriendsUpdate(){
		viewModelScope.launch {
			try{
				val responseFriends = TapasTopApi.retrofitService.getFriends(_currentUser.value!!.username)
				when(responseFriends.code()){
					200 -> _friends.value = responseFriends.body()
					else -> {}
				}
			}catch (e : Exception){
			}
		}
	}
	fun acceptRequest(username: String, pos: Int){
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try{
				val response = TapasTopApi.retrofitService.acceptFriendRequest(_currentUser.value!!.username, username )
				when(response.code()){
					200 -> {
						val user = _friendRequests.value!!.users.removeAt(pos)
						_friendRequests.value = response.body()
						_friends.value!!.users.add(_friends.value!!.users.size,user)
						_status.value = restApiStatus.NOTHING
					}
					400 -> {
						_status.value = restApiStatus.ERROR
					}
				}
			}catch (e : Exception){
				_status.value = restApiStatus.ERROR
			}
		}
	}
	fun declineRequest(username: String, pos : Int){
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try{
				val response = TapasTopApi.retrofitService.declineFriendRequest(_currentUser.value!!.username,username)
				when(response.code()){
					200 -> {
						_friendRequests.value!!.users.removeAt(pos)
						_friendRequests.value = response.body()
						_status.value = restApiStatus.NOTHING
					}
					400 -> {
						_status.value = restApiStatus.ERROR
					}
				}
			}catch (e : Exception){
				_status.value = restApiStatus.ERROR
			}
		}
	}
	/**
	 * Send request to the server to send verification email to the current user
	 */
	fun sendMail() {
		generateCode()
		viewModelScope.launch {
			try {
				val response = _currentUser.value?.let {
					TapasTopApi.retrofitService.sendVerificationMail(
						it.email, code
					)
				}
			} catch (e: Exception) {
				Log.d("Send email exception", "${e.printStackTrace()}")
			}
		}
	}

	fun createUser() {
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try {
				_currentUser.value?.profileImg = encodeImage(_userProfileImg.value)
				val newUser: UserResponse = _currentUser.value?.let { UserResponse(it) }!!
				val response = newUser?.let { TapasTopApi.retrofitService.createUser(it) }
				when (response?.code()) {
					200 -> _status.value = restApiStatus.DONE
					else -> _status.value = restApiStatus.ERROR
				}
			} catch (e: Exception) {
				_status.value = restApiStatus.ERROR
			}
		}
	}

	fun saveCurrentUser() {
		userBackup = _currentUser.value!!.copy()
	}

	private fun encodeImage(imageBitmap: Bitmap?): String {
		val baos = ByteArrayOutputStream()
		imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, baos)
		val b = baos.toByteArray()
		return Base64.encodeToString(b, Base64.DEFAULT)
	}

	private fun decodeImage(imageB64: String?): Bitmap {
		val imageBytes = Base64.decode(imageB64, Base64.DEFAULT)
		return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
	}


	fun setUsername(username: String) {
		_currentUser.value?.username = username
	}

	fun setUserEmail(email: String) {
		_currentUser.value?.email = email
	}

	fun setUserPassword(password: String) {
		_currentUser.value?.password = password
	}

	fun setName(name: String) {
		_currentUser.value?.name = name
	}

	fun setLastName(lastName: String) {
		_currentUser.value?.surname = lastName
	}

	fun setGender(gender: String) {
		_currentUser.value?.gender = gender
	}

	fun setCountry(country: String) {
		_currentUser.value?.country = country
	}

	fun setLocation(location: String) {
		_currentUser.value?.location = location
	}

	fun setDescription(description: String) {
		_currentUser.value?.description = description
	}

	private fun generateCode() {
		code = "${(0..9).random()}" +
				"${(0..9).random()}" +
				"${(0..9).random()}" +
				"${(0..9).random()}"

	}

	/**
	 * Set user profile image
	 *
	 * @param image profile image
	 */
	fun setProfileImage(image: Bitmap) {
		_userProfileImg.value = image
		profilePicIsSet = true
	}

	/**
	 * Set number of degustations added by the user
	 *
	 * @param degustationsNumber added by the user
	 */
	fun setDegustationsNumber(degustationsNumber: Int) {
		_degustationsAdded.value = degustationsNumber
	}

	/**
	 * Set number of locals added by the user
	 *
	 * @param localsNumber added by the user
	 */
	fun setLocalsNumber(localsNumber: Int) {
		_localsAdded.value = localsNumber
	}

	/**
	 *
	 * Set number of award acquired by the user
	 *
	 * @param awardsNumber of the user
	 */
	fun setAwardsNumber(awardsNumber: Int) {
		_awards.value = awardsNumber
	}

	fun hasGenderSet(): Boolean {
		return _currentUser.value?.gender.isNullOrEmpty()
	}

	fun hasProfilePicSet(): Boolean {
		return profilePicIsSet
	}
}