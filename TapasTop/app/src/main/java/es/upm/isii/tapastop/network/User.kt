package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "user")val user: User
)

data class User(
    @Json(name = "country")val country: String,
    @Json(name = "description")val description: String,
    @Json(name = "email")val email: String,
    @Json(name = "gender")val gender: String,
    @Json(name = "location")val location: String,
    @Json(name = "name")val name: String,
    @Json(name = "password")val password: String,
    @Json(name = "profile_img")val profileImg: String,
    @Json(name = "surname")val surname: String,
    @Json(name = "username")val username: String
)