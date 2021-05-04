package com.vision.app.service

import com.vision.app.domain.AppUser
import com.vision.app.exceptions.UserAlreadyExists
import com.vision.app.exceptions.UserNotFound
import com.vision.app.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class UserServiceTest {

    private val userRepository: UserRepository = mockk(relaxed = true)
    private val userService: UserService = UserService(userRepository = userRepository)

    @Test
    fun `Should return successfully result while calling findByNickName`() {

        val userObject = AppUser(nickName = AppUser1)
        every { userRepository.findByNickName(AppUser1) } returns userObject

        val result = userService.findByNickName(AppUser1)
        assertEquals(userObject, result)
    }

    @Test
    fun `Given list of users with nickname when users are valid then return users `() {

        val userList = listOf(AppUser(nickName = AppUser1), AppUser(nickName = "nickName"))
        every { userRepository.findByNickNameIn(users = userList.map { it.nickName }) } returns userList

        val result = userService.findUserIn(users = userList.map { it.nickName })
        assertEquals(userList, result)
    }

    @Test
    fun `Given list of users with nickname when one user are valid then throw UserNotFound exception `() {

        val userList = listOf(AppUser(nickName = AppUser1), AppUser(nickName = AppUser2))

        every { userRepository.findByNickNameIn(users = userList.map { it.nickName }) } returns listOf(userList[0])
        val exception: Exception = assertThrows(UserNotFound::class.java) {
            userService.findUserIn(userList.map { it.nickName })
        }

        val expected = "requested ${userList.map { it.nickName }} users and received ${listOf(userList[0])}"

        assertEquals(expected, exception.message)
    }

    @Test
    fun `Given user nickName when user is already present then throw UserAlreadyExists exception`() {

        val user = AppUser(nickName = AppUser1)
        every { userRepository.findByNickName(AppUser1) } returns AppUser(nickName = AppUser1)
        val exception: Exception = assertThrows(UserAlreadyExists::class.java) {
            userService.save(user)
        }
        val expectedMessage = "User ${user.nickName} already exists"
        assertEquals(expectedMessage, exception.message)
    }

    companion object {
        private const val AppUser1 = "user1"
        private const val AppUser2 = "user2"
    }
}
