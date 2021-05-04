package com.vision.app.repository

import com.vision.app.domain.AppUser
import com.vision.app.domain.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, String> {

    fun findMessagesBySender(user: AppUser): List<Message>

    fun findMessagesByReceiverAndSender(receiver: AppUser, sender: AppUser): List<Message>

    fun findMessagesByReceiver(user: AppUser): List<Message>
}
