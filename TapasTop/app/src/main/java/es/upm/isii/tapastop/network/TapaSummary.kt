package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class Tapas(
	@Json(name = "tastings") val tapas : MutableList<TapaSummary>
)

data class TapaSummary(
	@Json(name="id") val id : Int,
	@Json(name = "name") val name : String,
	@Json(name ="photo") val photo : String
)