package com.vision.app.domain

import java.time.Instant
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@Entity
@Table(name = "message")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val messageId: Int? = null,

    @ManyToOne
    @JoinColumn(name = "sender", insertable = true, nullable = false)
    val sender: AppUser,

    @ManyToOne
    @JoinColumn(name = "receiver", insertable = true, nullable = false)
    val receiver: AppUser,

    @Column
    val message: String,

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    val timestamp: Date = Date.from(Instant.now())
)
