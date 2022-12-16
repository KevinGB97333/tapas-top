package es.upm.isii.tapastop.network

import com.squareup.moshi.Json

data class Restaurants(
    @Json(name = "restaurants")val restaurants : List<Restaurant>
)