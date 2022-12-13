package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class Restaurants(
    @Json(name = "Restaurants")val restaurants : List<Restaurant>
)