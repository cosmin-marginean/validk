package com.test.controller.rest

import io.micronaut.core.annotation.Introspected
import io.validk.ValidObject
import io.validk.Validation
import io.validk.email
import io.validk.min
import io.validk.notBlank
import io.validk.validation

@Introspected
data class UpdateUserForm(
    val name: String,
    val age: Int,
    val email: String
) : ValidObject<UpdateUserForm> {

    override fun validation(): Validation<UpdateUserForm> {
        return validation<UpdateUserForm> {
            UpdateUserForm::name {
                notBlank() message "Please provide the user's full name"
            }
            UpdateUserForm::age {
                min(18) message "Age must be at least 18"
            }
            UpdateUserForm::email {
                notBlank() message "Email is required"
                email() message "This is not a valid email"
            }
        }
    }
}
