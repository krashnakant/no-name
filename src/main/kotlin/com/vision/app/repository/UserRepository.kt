package com.vision.app.repository

import com.vision.app.domain.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<AppUser, String> {

    fun findByNickNameIn(users: List<String>): List<AppUser>

    fun findByNickName(nickName: String): AppUser?
}
