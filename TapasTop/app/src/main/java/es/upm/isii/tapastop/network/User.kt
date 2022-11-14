package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class UserResponse(
	@Json(name = "user") val user: User
)

data class User(
	@Json(name = "country") var country: String,
	@Json(name = "description") var description: String,
	@Json(name = "email") var email: String,
	@Json(name = "gender") var gender: String,
	@Json(name = "location") var location: String,
	@Json(name = "name") var name: String,
	@Json(name = "password") var password: String,
	@Json(name = "profile_img") var profileImg: String,
	@Json(name = "surname") var surname: String,
	@Json(name = "username") var username: String
)