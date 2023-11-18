package io.validk

import io.kotest.core.spec.style.StringSpec

class Examples : StringSpec({

    "basic" {
        data class Employee(val name: String, val email: String?)
        data class Organisation(val name: String, val employees: List<Employee>)

        val validation = Validation {
            Organisation::name { minLength(5) }
            Organisation::employees each {
                Employee::name { minLength(10) }
                Employee::email ifNotNull { email() }
            }
        }

        val org = Organisation(
            "A", listOf(
                Employee("John", "john@test.com"),
                Employee("Hannah Johnson", "hanna")
            )
        )
        val errors = validation.validate(org)
        errors?.errors?.forEach { println(it) }
    }

    "valid object" {
        data class Person(val name: String, val email: String) : ValidObject<Person> {
            override val validation: Validation<Person> = Validation {
                Person::name { minLength(10) }
                Person::email { email() }
            }
        }

        Person("John Smith", "john@test.com").validate()
    }

    "custom messages" {
        data class Person(val name: String)

        Validation {
            Person::name {
                notBlank() message "A person needs a name"
                matches("[a-zA-Z\\s]+") message "Letters only please"
            }
        }
    }

    "context aware" {
        val validation: Validation<Entity> = Validation {
            Entity::entityType { enum<EntityType>() }

            Entity::entityType.whenIs("PERSON") {
                Entity::proofOfId { minLength(10) }
            }

            Entity::entityType.whenIs("COMPANY") {
                Entity::registeredOffice { minLength(5) }
            }
        }
    }

    "context aware - withValue" {
        Validation<Entity> {
            Entity::entityType { enum<EntityType>() }
            withValue { entity ->
                when (entity.entityType) {
                    "PERSON" -> Entity::proofOfId { minLength(10) }
                    "COMPANY" -> Entity::registeredOffice { minLength(5) }
                }
            }
        }
    }

    "validation object" {
        data class MyObject(val name: String, val age: Int) : ValidObject<MyObject> {
            override val validation: Validation<MyObject> = Validation {
                MyObject::name { notBlank() }
                MyObject::age { min(18) }
            }
        }

        val result = MyObject("John Smith", 12).validate()
    }

}) {
    private data class Entity(
        val entityType: String,
        val registeredOffice: String,
        val proofOfId: String
    )

    private enum class EntityType { COMPANY, PERSON }

    private data class MyObject(val name: String, val age: Int) : ValidObject<MyObject> {
        override val validation: Validation<MyObject> = Validation {
            MyObject::name { notBlank() }
            MyObject::age { min(18) }
        }
    }

}
