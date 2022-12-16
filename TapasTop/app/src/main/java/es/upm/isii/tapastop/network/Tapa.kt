package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class TapaResponse(
	@Json(name = "tasting") val tapa : Tapa
)

data class Tapa(
	@Json(name = "id") var id : Int,
	@Json(name = "photo") var photo : String,
	@Json(name = "name") var name : String,
	@Json(name = "description") var description : String,
	@Json(name = "author") var author : String,
	@Json(name = "type") var type : String,
	@Json(name = "region") var region : String,
	@Json(name = "country") var country : String,
	@Json(name = "taste") var taste : String,
	@Json(name = "date") var date : String,
	@Json(name = "restaurant") var restaurant : String,
	@Json(name ="average") var averageRate : Float,
	@Json(name="personal rating") var personalRate : Float
)
