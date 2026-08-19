package com.iolandarosa.retailhub.features.auth.data.mapper

import com.iolandarosa.retailhub.features.auth.data.model.UserDto
import com.iolandarosa.retailhub.features.auth.domain.model.User

internal fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = "${this.firstName} ${this.lastName}"
    )
}