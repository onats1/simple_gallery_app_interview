package com.example.simplegallery.utils

import com.example.simplegallery.data.remote.models.Location
import com.example.simplegallery.data.remote.models.PhotoRemote
import com.example.simplegallery.data.remote.models.Urls
import com.example.simplegallery.data.remote.models.UserData

val photoRemote1 = PhotoRemote(
    id = "1",
    urls = Urls(
        raw = "",
        full = "",
        regular = "https://example.com/photo1",
        small = "",
    ),
    location = Location(
        city = "",
        country = ""
    ),
    user = UserData(
        id = "",
        username = "",
        name = "",
    )
)
val photoRemote2 = PhotoRemote(
    id = "2",
    urls = Urls(
        raw = "https://example.com/photo2",
        full = "",
        regular = "",
        small = "",
    ),
    location = Location(
        city = "",
        country = ""
    ),
    user = UserData(
        id = "",
        username = "",
        name = "",
    )
)
val photoRemote3 = PhotoRemote(
    id = "3",
    urls = Urls(
        raw = "https://example.com/photo3",
        full = "",
        regular = "",
        small = "",
    ),
    location = Location(
        city = "",
        country = ""
    ),
    user = UserData(
        id = "",
        username = "",
        name = "",
    )
)