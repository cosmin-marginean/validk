package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ValidObjectTest : StringSpec({

    "valid object validation" {
        TestValidObject("", 12).validate() shouldBe errors(
            ValidationError("name", "cannot be blank"),
            ValidationError("age", "must be at least 18")
        )
        TestValidObject("john smith ", 20).validate() shouldBe null
    }

    "validation check" {
        data class Result(val success: Boolean)

        TestValidObject("", 12).validate {
            onError { Result(false) }
            onSuccess { Result(true) }
        } shouldBe Result(false)

        TestValidObject("John smith", 20).validate {
            onError { Result(false) }
            onSuccess { Result(true) }
        } shouldBe Result(true)
    }
}) {

    private data class TestValidObject(
        val name: String,
        val age: Int,
    ) : ValidObject<TestValidObject> {

        override val validation: Validation<TestValidObject> = Validation {
            TestValidObject::name {
                notBlank()
            }

            TestValidObject::age {
                min(18)
            }
        }
    }

}
