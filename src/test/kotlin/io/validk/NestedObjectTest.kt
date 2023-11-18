package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NestedObjectTest : StringSpec({

    "nested object" {
        val validation = Validation {
            Person::name { notBlank() }
            Person::address {
                Address::city { notBlank() }
                Address::postCode { minLength(4) }
            }
        }

        validation.validate(Person("", Address("London", "ABCD"))) shouldBe errors(ValidationError("name", "cannot be blank"))
        validation.validate(Person("", Address("", "ABCD"))) shouldBe errors(
            ValidationError("name", "cannot be blank"),
            ValidationError("address.city", "cannot be blank")
        )
        validation.validate(Person("", Address("", "ABC"))) shouldBe errors(
            ValidationError("name", "cannot be blank"),
            ValidationError("address.city", "cannot be blank"),
            ValidationError("address.postCode", "must be at least 4 characters")
        )

        validation.validate(Person("John Smith", Address("", "ABC"))) shouldBe errors(
            ValidationError("address.city", "cannot be blank"),
            ValidationError("address.postCode", "must be at least 4 characters")
        )

        validation.validate(Person("John Smith", Address("London", "ABCD"))) shouldBe null
    }

    "nested collection" {
        val validation = Validation<Parent> {
            Parent::name { notBlank() }
            Parent::children each {
                Child::childName { notBlank() }
            }
        }

        validation.validate(Parent("John Smith", listOf(Child("One"), Child("Two")))) shouldBe null
        validation.validate(Parent("John Smith", listOf(Child(""), Child("Two"), Child("")))) shouldBe errors(
            ValidationError("children[0].childName", "cannot be blank"),
            ValidationError("children[2].childName", "cannot be blank")
        )
    }
}) {

    private data class Person(
        val name: String,
        val address: Address
    )

    private data class Address(
        val city: String,
        val postCode: String,
    )

    private data class Parent(
        val name: String,
        val children: List<Child>
    )

    private data class Child(val childName: String)
}
