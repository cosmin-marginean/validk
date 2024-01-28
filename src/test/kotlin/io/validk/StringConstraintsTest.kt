package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class StringConstraintsTest : StringSpec({

    "notEmpty" {
        Validation { this.notEmptyString() }.validate("abc") shouldBe null
        Validation { this.notEmptyString() }.validate("  ") shouldBe null
        Validation { this.notEmptyString() }.validate("") shouldNotBe null
    }

    "notBlank" {
        Validation { notBlank() }.validate("abc") shouldBe null
        Validation { notBlank() }.validate("  abc ") shouldBe null
        Validation { notBlank() }.validate("  ") shouldNotBe null
        Validation { notBlank() }.validate("") shouldNotBe null
    }

    "minLength" {
        Validation { minLength(5) }.validate("abcde") shouldBe null
        Validation { minLength(5) }.validate("abcd") shouldNotBe null
        Validation { minLength(5) }.validate("a") shouldNotBe null
        Validation { minLength(5) }.validate(" ") shouldNotBe null
        Validation { minLength(5) }.validate("") shouldNotBe null
    }

    "maxLength" {
        Validation { maxLength(5) }.validate("abcdef") shouldNotBe null
        Validation { maxLength(5) }.validate("abcde") shouldBe null
        Validation { maxLength(5) }.validate("abcd") shouldBe null
        Validation { maxLength(5) }.validate("a") shouldBe null
        Validation { maxLength(5) }.validate(" ") shouldBe null
        Validation { maxLength(5) }.validate("") shouldBe null
    }

    "matches" {
        Validation { matches("[a-zA-Z]+") }.validate("abcdef") shouldBe null
        Validation { matches("[a-zA-Z]+") }.validate("abcdef ") shouldNotBe null
        Validation { matches("[a-zA-Z]+") }.validate("abc1ef") shouldNotBe null
    }

    "email" {
        Validation { email() }.validate("test") shouldNotBe null
        Validation { email() }.validate("test@domain") shouldNotBe null
        Validation { email() }.validate("test@domain.com") shouldBe null

        Validation { email() }.validate("") shouldNotBe null
        Validation { email() }.validate("")!!.eagerErrors shouldBe listOf(ValidationError("Object", "must be a valid email"))
        Validation { email() }.validate("test")!!.eagerErrors shouldBe listOf(ValidationError("Object", "must be a valid email"))
    }

    "enum" {
        Validation { enum("PERSON", "COMPANY") }.validate("PERSON") shouldBe null
        Validation { enum("PERSON", "COMPANY") }.validate("OTHER") shouldNotBe null
        Validation { enum<TestEnum>() }.validate("COMPANY") shouldBe null
        Validation { enum<TestEnum>() }.validate("OTHER") shouldNotBe null
    }
}) {
    private enum class TestEnum {
        PERSON,
        COMPANY
    }
}
