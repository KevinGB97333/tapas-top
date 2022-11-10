package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class MessageResponse(
    @Json(name="mensaje")val message: String
)
