package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class CreateResponse(
    @Json(name="mensaje")val message: String
)
