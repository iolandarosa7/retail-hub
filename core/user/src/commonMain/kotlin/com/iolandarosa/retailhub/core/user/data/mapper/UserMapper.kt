/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.data.mapper

import com.iolandarosa.retailhub.core.user.data.model.UserDto
import com.iolandarosa.retailhub.core.user.domain.model.Address
import com.iolandarosa.retailhub.core.user.domain.model.Coordinates
import com.iolandarosa.retailhub.core.user.domain.model.User

internal fun UserDto.toDomain(): User =
    User(
        id = this.id,
        name = "${this.firstName} ${this.lastName}",
        image = this.image,
        role = this.role,
        email = this.email,
        phone = this.phone,
        age = this.age,
        gender = this.gender,
        birthDate = this.birthDate,
        bloodGroup = this.bloodGroup,
        height = this.height,
        weight = this.weight,
        eyeColor = this.eyeColor,
        hairColor = this.hair.color,
        hairType = this.hair.type,
        address =
            Address(
                street = this.address.address,
                city = this.address.city,
                state = this.address.state,
                stateCode = this.address.stateCode,
                postalCode = this.address.postalCode,
                country = this.address.country,
                coordinates =
                    Coordinates(
                        lat = this.address.coordinates.lat,
                        lng = this.address.coordinates.lng,
                    ),
            ),
    )
