package dev.bettercode.audit.repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class AuditLog(
    @Id
    @GeneratedValue
    val id: Long? = null,
    val msg: String? = null
)