package com.iolandarosa.retailhub.features.auth.data.mapper

import com.iolandarosa.retailhub.features.auth.data.response.UserDto
import com.iolandarosa.retailhub.features.auth.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = "${this.firstName} ${this.lastName}"
    )
}