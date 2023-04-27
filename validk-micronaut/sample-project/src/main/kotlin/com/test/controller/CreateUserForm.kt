package com.test.controller

import io.micronaut.core.annotation.Introspected
import io.validk.ValidObject
import io.validk.Validation
import io.validk.email
import io.validk.min
import io.validk.notBlank
import io.validk.validation

@Introspected
data class CreateUserForm(
    val name: String,
    val age: Int,
    val email: String
) : ValidObject<CreateUserForm> {

    override fun validation(): Validation<CreateUserForm> {
        return validation<CreateUserForm> {
            CreateUserForm::name {
                notBlank() message "Please provide the user's full name"
            }
            CreateUserForm::age {
                min(18) message "Age must be at least 18"
            }
            CreateUserForm::email {
                notBlank() message "Email is required"
                email() message "This is not a valid email"
            }
        }
    }
}
