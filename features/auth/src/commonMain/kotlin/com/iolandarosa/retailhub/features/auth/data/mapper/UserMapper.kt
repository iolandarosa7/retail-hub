/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.data.mapper

import com.iolandarosa.retailhub.features.auth.data.model.UserDto
import com.iolandarosa.retailhub.features.auth.domain.model.User

internal fun UserDto.toDomain(): User =
    User(
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
        address = this.address.address,
        city = this.address.city,
        country = this.address.country,
    )
