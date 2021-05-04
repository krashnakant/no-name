package com.vision.app.service

import com.vision.app.domain.AppUser
import com.vision.app.exceptions.UserAlreadyExists
import com.vision.app.exceptions.UserNotFound
import com.vision.app.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) : IBaseService<AppUser, String> {
    override fun findById(id: String): AppUser? {
        return userRepository.findByIdOrNull(id) ?: throw UserNotFound("requested $id user")
    }

    override fun <S : AppUser> save(entity: S): S {
        userRepository.findByNickName(entity.nickName)
            ?.let { throw UserAlreadyExists("User ${entity.nickName} already exists") }
        return userRepository.save(entity)
    }

    fun findByNickName(nickName: String): AppUser {
        return userRepository.findByNickName(nickName = nickName) ?: throw UserNotFound("requested $nickName user")
    }

    fun findUserIn(users: List<String>): List<AppUser> {
        val domainUser = userRepository.findByNickNameIn(users = users)
        return if (users.size == domainUser.size) domainUser
        else throw UserNotFound("requested $users users and received $domainUser")
    }
}
