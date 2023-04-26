package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ValidObjectTest : StringSpec({

    "valid object validation" {
        BasicValidObject("", 12).validate() shouldBe errors(
            ValidationError("name", "cannot be blank"),
            ValidationError("age", "must be at least 18")
        )
    }
}) {

    private data class BasicValidObject(
        val name: String,
        val age: Int,
    ) : ValidObject<BasicValidObject> {

        override fun validation(): Validation<BasicValidObject> {
            return validation {
                BasicValidObject::name {
                    notBlank()
                }

                BasicValidObject::age {
                    min(18)
                }
            }
        }
    }

}
