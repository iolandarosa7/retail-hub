/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.profile.utils

import com.iolandarosa.retailhub.features.profile.domain.model.Address
import com.iolandarosa.retailhub.features.profile.domain.model.Coordinates
import com.iolandarosa.retailhub.features.profile.domain.model.User

object TestUser {
    val user =
        User(
            id = 1,
            name = "John Doe",
            image = "image_url",
            role = "admin",
            email = "john@example.com",
            phone = "123456",
            age = 30,
            gender = "male",
            birthDate = "2000-01-01",
            bloodGroup = "A+",
            height = 180.0,
            weight = 80.0,
            eyeColor = "brown",
            hairColor = "black",
            hairType = "straight",
            address =
                Address(
                    street = "address",
                    city = "city",
                    state = "state",
                    stateCode = "stateCode",
                    postalCode = "postalCode",
                    coordinates = Coordinates(lat = 1.0, lng = 1.0),
                    country = "country",
                ),
        )
}
