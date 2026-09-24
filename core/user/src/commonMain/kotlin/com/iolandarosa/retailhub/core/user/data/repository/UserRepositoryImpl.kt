/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.user.data.repository

import com.iolandarosa.retailhub.core.model.NetworkResult
import com.iolandarosa.retailhub.core.storage.domain.LocalImageStorage
import com.iolandarosa.retailhub.core.user.data.mapper.toDomain
import com.iolandarosa.retailhub.core.user.data.remote.UserRemoteDataSource
import com.iolandarosa.retailhub.core.user.data.remote.UserRemoteDataSourceImpl
import com.iolandarosa.retailhub.core.user.domain.model.User
import com.iolandarosa.retailhub.core.user.domain.repository.UserRepository

internal class UserRepositoryImpl(
    private val service: UserRemoteDataSource,
    private val localImageStorage: LocalImageStorage,
) : UserRepository {
    override suspend fun getAuthUser(): NetworkResult<User> = service.getAuthUser().map { it.toDomain() }

    override suspend fun getLocalUserImage(userId: Int): ByteArray? = localImageStorage.load("$userId")
}
