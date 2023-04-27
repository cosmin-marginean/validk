package io.validk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class StringConstraintsTest : StringSpec({

    "notEmpty" {
        validation { notEmpty() }.validate("abc") shouldBe null
        validation { notEmpty() }.validate("  ") shouldBe null
        validation { notEmpty() }.validate("") shouldNotBe null
    }

    "notBlank" {
        validation { notBlank() }.validate("abc") shouldBe null
        validation { notBlank() }.validate("  abc ") shouldBe null
        validation { notBlank() }.validate("  ") shouldNotBe null
        validation { notBlank() }.validate("") shouldNotBe null
    }

    "minLength" {
        validation { minLength(5) }.validate("abcde") shouldBe null
        validation { minLength(5) }.validate("abcd") shouldNotBe null
        validation { minLength(5) }.validate("a") shouldNotBe null
        validation { minLength(5) }.validate(" ") shouldNotBe null
        validation { minLength(5) }.validate("") shouldNotBe null
    }

    "maxLength" {
        validation { maxLength(5) }.validate("abcdef") shouldNotBe null
        validation { maxLength(5) }.validate("abcde") shouldBe null
        validation { maxLength(5) }.validate("abcd") shouldBe null
        validation { maxLength(5) }.validate("a") shouldBe null
        validation { maxLength(5) }.validate(" ") shouldBe null
        validation { maxLength(5) }.validate("") shouldBe null
    }

    "matches" {
        validation { matches("[a-zA-Z]+") }.validate("abcdef") shouldBe null
        validation { matches("[a-zA-Z]+") }.validate("abcdef ") shouldNotBe null
        validation { matches("[a-zA-Z]+") }.validate("abc1ef") shouldNotBe null
    }

    "email" {
        validation { email() }.validate("test") shouldNotBe null
        validation { email() }.validate("test@domain") shouldNotBe null
        validation { email() }.validate("test@domain.com") shouldBe null

        validation { email() }.validate("") shouldNotBe null
        validation { email() }.validate("")!!.eagerErrors shouldBe listOf(ValidationError("Object", "must be a valid email"))
        validation { email() }.validate("test")!!.eagerErrors shouldBe listOf(ValidationError("Object", "must be a valid email"))
    }

    "enum" {
        validation { enum("PERSON", "COMPANY") }.validate("PERSON") shouldBe null
        validation { enum("PERSON", "COMPANY") }.validate("OTHER") shouldNotBe null
        validation { enum<TestEnum>() }.validate("COMPANY") shouldBe null
        validation { enum<TestEnum>() }.validate("OTHER") shouldNotBe null
    }
}) {
    private enum class TestEnum {
        PERSON,
        COMPANY
    }
}