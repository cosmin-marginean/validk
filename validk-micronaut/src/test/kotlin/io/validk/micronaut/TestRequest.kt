package io.validk.micronaut

import io.micronaut.core.annotation.Introspected
import io.validk.ValidObject
import io.validk.Validation
import io.validk.minLength
import io.validk.notBlank

@Introspected
data class TestRequest(val name: String) : ValidObject<TestRequest> {
    override val validation: Validation<TestRequest> = Validation {
        TestRequest::name {
            notBlank() message "Name is required"
            minLength(5) message "Name should be at least 5 characters long"
        }
    }
}
