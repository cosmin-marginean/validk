package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ConditionalValidation : StringSpec({

    "validate fields based on the value of another field" {
        val validation = Validation<Entity> {
            Entity::entityType { enum<EntityType>() }

            withValue { entity ->
                when (entity.entityType) {
                    "PERSON" -> {
                        Entity::proofOfId { minLength(10) }
                        Entity::age { min(18) }
                    }

                    "COMPANY" -> Entity::registeredOffice { minLength(5) }
                }
            }
        }

        validation.validate(Entity("PERSON", "", "Passport number 0123456789", 12)) shouldBe errors(
            ValidationError("age", "must be at least 18")
        )
        validation.validate(Entity("COMPANY", "", "", 12)) shouldBe errors(
            ValidationError("registeredOffice", "must be at least 5 characters")
        )

        validation.validate(Entity("PERSON", "", "Passport number 0123456789", 24)) shouldBe null
        validation.validate(Entity("COMPANY", "London", "", 1)) shouldBe null

        validation.validate(Entity("NOTHING", "London", "", 1)) shouldBe errors(
            ValidationError("entityType", "must be one of: COMPANY, PERSON")
        )
    }

    "validate fields based on the value of another field with whenIs" {
        val validation = Validation {
            Entity::entityType { enum<EntityType>() }

            Entity::entityType.whenIs("PERSON") {
                Entity::proofOfId { minLength(10) }
                Entity::age { min(18) }
            }

            Entity::entityType.whenIs("COMPANY") {
                Entity::registeredOffice { minLength(5) }
            }
        }

        validation.validate(Entity("PERSON", "", "Passport number 0123456789", 12)) shouldBe errors(
            ValidationError("age", "must be at least 18")
        )
        validation.validate(Entity("COMPANY", "", "", 12)) shouldBe errors(
            ValidationError("registeredOffice", "must be at least 5 characters")
        )

        validation.validate(Entity("PERSON", "", "Passport number 0123456789", 24)) shouldBe null
        validation.validate(Entity("COMPANY", "London", "", 1)) shouldBe null

        validation.validate(Entity("NOTHING", "London", "", 1)) shouldBe errors(
            ValidationError("entityType", "must be one of: COMPANY, PERSON")
        )
    }


    "validate when collection contains" {
        val validation = Validation {
            EntityWithCollectionField::roles.whenContains("ADMIN") {
                EntityWithCollectionField::adminEmail { notBlank() message "Email admin is required when roles contains ADMIN" }
            }
        }

        validation.validate(EntityWithCollectionField(setOf("USER", "HR"), "")) shouldBe null
        validation.validate(EntityWithCollectionField(setOf("USER", "ADMIN"), "")) shouldBe errors(
            ValidationError("adminEmail", "Email admin is required when roles contains ADMIN")
        )
    }

    "validate when collection contains while also having collection validation" {
        val validation = Validation {
            EntityWithCollectionField::roles {
                notEmpty() message "Roles required"
            }
            EntityWithCollectionField::roles.whenContains("ADMIN") {
                EntityWithCollectionField::adminEmail { notBlank() message "Email admin is required when roles contains ADMIN" }
            }
        }

        validation.validate(EntityWithCollectionField(emptySet(), "")) shouldBe errors(
            ValidationError("roles", "Roles required")
        )
        validation.validate(EntityWithCollectionField(setOf("USER", "HR"), "")) shouldBe null
        validation.validate(EntityWithCollectionField(setOf("USER", "ADMIN"), "")) shouldBe errors(
            ValidationError("adminEmail", "Email admin is required when roles contains ADMIN")
        )
    }
}) {

    private data class Entity(
        val entityType: String,
        val registeredOffice: String,
        val proofOfId: String,
        val age: Int
    )

    private data class EntityWithCollectionField(
        val roles: Set<String>,
        val adminEmail: String
    )

    private enum class EntityType {
        COMPANY,
        PERSON
    }
}
