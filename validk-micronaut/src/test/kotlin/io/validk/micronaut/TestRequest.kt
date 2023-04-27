package io.validk.micronaut

import io.micronaut.core.annotation.Introspected
import io.validk.ValidObject
import io.validk.Validation
import io.validk.minLength
import io.validk.notBlank
import io.validk.validation

@Introspected
data class TestRequest(val name: String) : ValidObject<TestRequest> {
    override fun validation(): Validation<TestRequest> {
        return validation {
            TestRequest::name {
                notBlank() message "Name is required"
                minLength(5) message "Name should be at least 5 characters long"
            }
        }
    }
}
