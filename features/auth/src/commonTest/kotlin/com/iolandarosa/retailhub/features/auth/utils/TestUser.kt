/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.utils

import com.iolandarosa.retailhub.features.auth.domain.model.User

object TestUser {
    val user =
        User(
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
            address = "123 Main St",
            city = "Lisbon",
            country = "Portugal",
        )
}
