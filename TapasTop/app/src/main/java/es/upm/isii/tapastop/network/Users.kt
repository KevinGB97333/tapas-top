package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class Users(
	@Json(name = "users") val users: MutableList<UserSummary>
)