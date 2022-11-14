package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class UserSummary(
    @Json(name="profile_img")val profile_img: String,
    @Json(name = "username")val username: String
)