package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class BasicPropertiesTest : StringSpec({

    "one field failed, one error" {
        Person(name = "John Smith", age = 12).validate {
            Person::name { notBlank() }
            Person::age { min(18) }
        } shouldBe errors(ValidationError("age", "must be at least 18"))
    }

    "one field failed, one error - create validation object first" {
        val validation = validation<Person> {
            Person::name { notBlank() }
            Person::age { min(18) }
        }
        validation.validate(Person(name = "John Smith", age = 12)) shouldBe errors(ValidationError("age", "must be at least 18"))
    }

    "one field failed, multiple errors" {
        Person(name = "", age = 23).validate {
            Person::name {
                notBlank()
                matches("[a-zA-Z]+ [a-zA-Z]+")
            }
            Person::age { min(18) }
        } shouldBe errors(
            ValidationError("name", "cannot be blank"),
            ValidationError("name", "must match pattern [a-zA-Z]+ [a-zA-Z]+")
        )
    }

    "one field failed, multiple errors, first error only" {
        Person(name = "", age = 23).validate {
            Person::name {
                notBlank()
                matches("[a-zA-Z]+ [a-zA-Z]+")
            }
            Person::age { min(18) }
        }.eagerErrors shouldBe listOf(ValidationError("name", "cannot be blank"))
    }

    "custom error message" {
        Person(name = "", age = 23).validate {
            Person::name {
                notBlank() message "Really now?"
                matches("[a-zA-Z]+ [a-zA-Z]+") message "Characters only please"
            }
            Person::age { min(18) }
        } shouldBe errors(
            ValidationError("name", "Really now?"),
            ValidationError("name", "Characters only please")
        )
    }
}) {

    private data class Person(
        val name: String,
        val age: Int,
    )
}

