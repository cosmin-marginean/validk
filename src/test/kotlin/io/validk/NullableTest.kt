package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NullableTest : StringSpec({

    "validate if not null" {
        val validation = Validation(failFast = false) {
            Person::name { notBlank() }
            Person::email ifNotNull {
                notBlank()
                email()
            }
        }

        validation.validate(Person("John Smith", null)) shouldBe null
        validation.validate(Person("John Smith", "")) shouldBe errors(
            ValidationError("email", "cannot be blank"),
            ValidationError("email", "must be a valid email")
        )
        validation.validate(Person("John Smith", "aaa")) shouldBe errors(ValidationError("email", "must be a valid email"))
        validation.validate(Person("John Smith", "john.smith@test")) shouldBe errors(ValidationError("email", "must be a valid email"))
        validation.validate(Person("John Smith", "john.smith@test.com")) shouldBe null
    }

    "cannot be null" {
        val validation = Validation(failFast = false) {
            Person::name { notBlank() }
            Person::email notNull {
                email()
            }
        }
        validation.validate(Person("John Smith", null)) shouldBe errors(ValidationError("email", "is required"))
        validation.validate(Person("John Smith", "aaa")) shouldBe errors(ValidationError("email", "must be a valid email"))
    }

    "cannot be null custom message" {
        val validation = Validation(failFast = false) {
            Person::name { notBlank() }
            Person::email.notNull("Please enter an email") {
                email()
            }
        }
        validation.validate(Person("John Smith", null)) shouldBe errors(ValidationError("email", "Please enter an email"))
        validation.validate(Person("John Smith", "aaa")) shouldBe errors(ValidationError("email", "must be a valid email"))
    }
}) {

    private data class Person(
        val name: String,
        val email: String?
    )
}
