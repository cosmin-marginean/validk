package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ComplexNestingTest : StringSpec({

    "complex validation" {
        val validation = Validation<Organisation> {
            Organisation::name { notBlank() }
            Organisation::employees each {
                Employee::name { notBlank() }
                Employee::email { email() }
                Employee::phoneNumber ifNotNull { minLength(6) }
                Employee::roles each {
                    Role::name { enum("DIRECTOR", "EMPLOYEE") }
                    Role::types {
                        notEmpty()
                    }
                }
                Employee::address {
                    Address::city { notBlank() }
                    Address::postCode { minLength(5) }
                }
            }
        }

        validation.validate(
            Organisation(
                "ACME",
                listOf(
                    Employee(
                        "John Smith",
                        "john@smith.com",
                        "123567890",
                        listOf(Role("DIRECTOR", listOf("employee", "leadership"))),
                        Address("London", "12345")
                    ),
                    Employee(
                        "Angela White",
                        "angela@white.com",
                        "12356789",
                        listOf(Role("DIRECTOR", emptyList())),
                        Address("London", "12")
                    )
                )
            )
        ) shouldBe errors(
            ValidationError("employees[1].roles[0].types", "cannot be empty"),
            ValidationError("employees[1].address.postCode", "must be at least 5 characters")
        )
    }

}) {

    private data class Organisation(
        val name: String,
        val employees: List<Employee>
    )

    private data class Employee(
        val name: String,
        val email: String,
        val phoneNumber: String?,
        val roles: List<Role>,
        val address: Address,
    )

    private data class Role(
        val name: String,
        val types: List<String>
    )

    data class Address(
        val city: String,
        val postCode: String
    )
}
