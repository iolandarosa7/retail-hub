package com.iolandarosa.retailhub.features.auth.data.mappers

import com.iolandarosa.retailhub.features.auth.data.responses.UserDTO
import com.iolandarosa.retailhub.features.auth.domain.models.User

fun UserDTO.toDomain(): User {
    return User(
        id = this.id,
        name = "${this.firstName} ${this.lastName}"
    )
}