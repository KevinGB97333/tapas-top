package es.upm.isii.tapastop.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.ETC1.decodeImage
import android.util.Base64
import android.util.Log
import androidx.lifecycle.*
import es.upm.isii.tapastop.network.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log


class TapaViewModel : ViewModel() {

	private val _currentTapa = MutableLiveData<Tapa>()
	val currentTapa: LiveData<Tapa> = _currentTapa

	//Image of the current tapa
	private val _tapaImg = MutableLiveData<Bitmap>()
	val tapaImg: LiveData<Bitmap> = _tapaImg

	private val _status = MutableLiveData<restApiStatus>()
	val status: LiveData<restApiStatus> = _status

	private val _tapaGetStatus = MutableLiveData<userGetApiStatus>()
	val tapaGetStatus : LiveData<userGetApiStatus> = _tapaGetStatus

	private val _tapas = MutableLiveData<Tapas>()
	val tapas: LiveData<Tapas> = _tapas

	var restaurants: List<Restaurant> = listOf()
	init {
		resetTapa()
	}
	fun resetStatus(){
		_status.value = restApiStatus.NOTHING
		_tapaGetStatus.value = userGetApiStatus.NOTHING
	}
	fun resetTapas(){
		_currentTapa.value = Tapa(0,"", "", "", "", "", "", "", "", "", "",  0.0F, 0.0F)
		_tapas.value = Tapas(mutableListOf())
	}
	fun resetTapa() {
		_currentTapa.value = Tapa(0,"", "", "", "", "", "", "", "", "", "",  0.0F, 0.0F)
		_tapas.value = Tapas(mutableListOf())
		resetStatus()
		restaurants = listOf()

	}

	fun getRestaurants() {
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try {
				val response = TapasTopApi.retrofitService.getRestaurants()
				when (response.code()) {
					200 -> {
						restaurants = response.body()!!.restaurants
						_status.value = restApiStatus.DONE
					}
					else -> {
						_status.value = restApiStatus.ERROR
					}
				}
			} catch (e: Exception) {
				e.printStackTrace()
				_status.value = restApiStatus.ERROR
			}
		}
	}

	/**
	 * Retrieve all the ta	pas from the server that matches the string pattern specified
	 *
	 * @param searchString pattern of the tapas we want to retrieve
	 */
	fun getTapas(searchString: String) {
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try {
				val response = TapasTopApi.retrofitService.getTapaLike(searchString)
				when (response.code()) {
					200 -> {
						_tapas.value = response.body()
						_status.value = restApiStatus.DONE
					}
					else -> {
						_status.value = restApiStatus.ERROR
						_tapas.value = Tapas(mutableListOf())
					}
				}
			} catch (e: Exception) {
				_status.value = restApiStatus.ERROR
				_tapas.value = Tapas(mutableListOf())
			}
		}
	}

	fun updateRate(username: String) {
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try {
				val response = TapasTopApi.retrofitService.changeTapaRate(
					_currentTapa.value!!.personalRate,
					username,
					_currentTapa!!.value!!.id
				)
				when (response!!.code()) {
					200 -> {
						_status.value = restApiStatus.DONE
					}
					else -> {
						_status.value = restApiStatus.ERROR
					}
				}
			} catch (e: Exception) {
				_status.value = restApiStatus.ERROR
			}
		}
	}

	/**
	 * Retrieve one tapa from the server if exist with all of it information
	 *
	 */
	fun getSpecificTapa(id: Int, username : String) {
		viewModelScope.launch {
			_tapaGetStatus.value = userGetApiStatus.LOADING
			try {
				val response = TapasTopApi.retrofitService.getTapa(id,username)
				when (response.code()) {
					200 -> {
						_currentTapa.value = response.body()!!.tapa
						_tapaImg.value = decodeImage(_currentTapa.value?.photo)
						Log.d("Current Tapa","${_currentTapa.value}")
						_tapaGetStatus.value = userGetApiStatus.DONE
					}
					else -> {
						_tapaGetStatus.value = userGetApiStatus.NOTHING
					}
				}

			} catch (e: Exception) {
				e.printStackTrace()
				_tapaGetStatus.value = userGetApiStatus.NOTHING
			}
		}
	}

	/**
	 * Send request to the server to create a tapa with the sent values
	 *
	 */
	fun createTapa() {
		viewModelScope.launch {
			_status.value = restApiStatus.LOADING
			try {
				_currentTapa.value?.photo = encodeImage(_tapaImg.value)
				val newTapa: TapaResponse = _currentTapa.value?.let { TapaResponse(it) }!!
				val response = newTapa.let {
					TapasTopApi.retrofitService.createTapa(
						it.tapa.personalRate, it
					)
				}
				when (response!!.code()) {
					200 -> {
						_status.value = restApiStatus.DONE
					}
					else -> _status.value = restApiStatus.ERROR

				}
			} catch (e: Exception) {
				e.printStackTrace()
				_status.value = restApiStatus.ERROR
			}
		}
	}

	fun setTapaImage(image: Bitmap) {
		_tapaImg.value = image
	}

	fun setName(name: String) {
		_currentTapa.value?.name = name
	}

	fun setDescription(description: String) {
		_currentTapa.value?.description = description
	}

	fun setAuthor(author: String) {
		_currentTapa.value?.author = author
	}

	fun setType(type: String) {
		_currentTapa.value?.type = type
	}

	fun setRegion(region: String) {
		_currentTapa.value?.region = region
	}

	fun setCountry(country: String) {
		_currentTapa.value?.country = country
	}

	fun setTaste(taste: String) {
		_currentTapa.value?.taste = taste
	}

	fun setDate() {
		val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
		val currentDate = LocalDateTime.now().format(formatter)
		_currentTapa.value?.date = currentDate
	}

	fun setRestaurant(restaurant: String) {
		_currentTapa.value?.restaurant = restaurant
	}

	fun setUserRate(rate: Float) {
		_currentTapa.value!!.personalRate = rate
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

}