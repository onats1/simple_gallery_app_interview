package com.example.simplegallery.network

data class PhotoRemote(
    val id: String,
    val urls: Urls,
    val location: Location,
    val user: UserData

)

data class Urls(
    val raw: String?,
    val full: String?,
    val regular: String?,
    val small: String?
)

data class Location(
    val city: String?,
    val country: String?
)

data class UserData(
    val id: String,
    val username: String,
    val name: String,

)