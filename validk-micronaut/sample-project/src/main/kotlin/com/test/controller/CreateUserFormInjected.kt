package com.test.controller

import io.micronaut.core.annotation.Introspected
import io.validk.ValidObject
import io.validk.Validation
import io.validk.email
import io.validk.min
import io.validk.notBlank
import io.validk.validation
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Introspected
data class CreateUserFormInjected(val name: String, val age: Int, val email: String) : ValidObject<CreateUserFormInjected> {

    @Inject
    lateinit var userService: UserService

    override fun validation(): Validation<CreateUserFormInjected> {
        return validation<CreateUserFormInjected> {
            CreateUserFormInjected::name {
                notBlank() message "Please provide the user's full name"
            }
            CreateUserFormInjected::age {
                min(18) message "Age must be at least 18"
            }
            CreateUserFormInjected::email {
                notBlank() message "Email is required"
                email() message "This is not a valid email"
                addConstraint("This email is already in use") { !userService.emailExists(it) }
            }
        }
    }
}

@Singleton
class UserService {

    fun emailExists(email: String): Boolean {
        return false
    }
}