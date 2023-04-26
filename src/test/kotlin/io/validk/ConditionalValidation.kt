package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ConditionalValidation : StringSpec({

    "validate fields based on the value of the value of another field" {
        val validation = validation<Entity> {
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
            ValidationError(
                "age",
                "must be at least 18"
            )
        )
        validation.validate(Entity("COMPANY", "", "", 12)) shouldBe errors(
            ValidationError(
                "registeredOffice",
                "must be at least 5 characters"
            )
        )

        validation.validate(Entity("PERSON", "", "Passport number 0123456789", 24)).success shouldBe true
        validation.validate(Entity("COMPANY", "London", "", 1)).success shouldBe true

        validation.validate(Entity("NOTHING", "London", "", 1)) shouldBe errors(
            ValidationError(
                "entityType",
                "must be one of: COMPANY, PERSON"
            )
        )
    }

    "validate fields based on the value of the value of another field with whenIs" {
        val validation = validation<Entity> {
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
            ValidationError(
                "age",
                "must be at least 18"
            )
        )
        validation.validate(Entity("COMPANY", "", "", 12)) shouldBe errors(
            ValidationError(
                "registeredOffice",
                "must be at least 5 characters"
            )
        )

        validation.validate(Entity("PERSON", "", "Passport number 0123456789", 24)).success shouldBe true
        validation.validate(Entity("COMPANY", "London", "", 1)).success shouldBe true

        validation.validate(Entity("NOTHING", "London", "", 1)) shouldBe errors(
            ValidationError(
                "entityType",
                "must be one of: COMPANY, PERSON"
            )
        )
    }
}) {

    private data class Entity(
        val entityType: String,
        val registeredOffice: String,
        val proofOfId: String,
        val age: Int
    )

    private enum class EntityType {
        COMPANY,
        PERSON
    }
}
